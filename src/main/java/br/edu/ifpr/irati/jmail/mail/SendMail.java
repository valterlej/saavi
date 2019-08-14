/*
 * Copyright 2019 Valter Luís Estevam Junior
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 * 
 */
package br.edu.ifpr.irati.jmail.mail;

import br.edu.ifpr.irati.jmail.configuracao.Propriedade;
import br.edu.ifpr.irati.jmail.configuracao.Configuracao;
import br.edu.ifpr.irati.jmail.configuracao.Conexao;
import br.edu.ifpr.irati.jmail.exception.SendMailException;
import java.io.File;
import java.util.List;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.internet.AddressException;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;

/**
 * Classe utilizada para encaminhar um e-mail.
 * 
 * @author Valter Estevam
 */
public class SendMail {

    /**
     * Relação de configurações de conexão a serem tentadas.
     * 
     */
    private final List<Conexao> conexoes;

    /**
     * Construtor padrão.
     * 
     * @param configuracao 
     */
    public SendMail(Configuracao configuracao) {
        this.conexoes = configuracao.getConexoes();
    }

    /**
     * Método para encaminhar uma mensagem de e-mail.
     * 
     * @param email
     * @throws SendMailException 
     */
    public void sendMail(Email email) throws SendMailException {

        Properties props = new Properties();

        boolean sucesso = false;

        for (Conexao conexao : conexoes) {

            String mailSMTPServer = "";

            props.clear();
            for (Propriedade p : conexao.getPropriedades()) {
                props.put(p.getNome(), p.getValor());
                if (p.getNome().equals("mail.smtp.host")) {
                    mailSMTPServer = p.getValor();
                }
            }
            if (mailSMTPServer.equals("")) {
                continue;
            }

            SimpleAuth auth = null;
            auth = new SimpleAuth(conexao.getAutenticacao().getUsuario(), conexao.getAutenticacao().getSenha());

            Session session = Session.getInstance(props, auth);//getDefaultInstance?
            session.setDebug(true);

            Message msg = new MimeMessage(session);

            // para admitir o envio para mais de um destinatário ao mesmo tempo
            InternetAddress dests[] = new InternetAddress[email.getDestinos().size()];
            int i = 0;
            for (String destino : email.getDestinos()) {
                try {
                    dests[i++] = new InternetAddress(destino.trim().toLowerCase());
                } catch (AddressException ex) {
                    throw new SendMailException("Endereço de e-mail inválido!");
                }
            }

            try {
                
                msg.setRecipients(Message.RecipientType.TO, dests);
                
                msg.setFrom(new InternetAddress(conexao.getAutenticacao().getUsuario()));
                             
                msg.setSubject(email.getAssunto());

                BodyPart messageBodyPart = new MimeBodyPart();                
                
                messageBodyPart.setContent(email.getTexto(),"text/html; charset=utf-8");                                                
                
                Multipart multipart = new MimeMultipart();
                
                multipart.addBodyPart(messageBodyPart);                
                                                
                for (File arquivo: email.getAnexos()){
                    messageBodyPart = new MimeBodyPart();
                    DataSource source = new FileDataSource(arquivo);
                    messageBodyPart.setDataHandler(new DataHandler(source));  
                    messageBodyPart.setFileName(source.getName());
                    multipart.addBodyPart(messageBodyPart);
                }
                
                msg.setContent(multipart);
                
            } catch (Exception e) {
                System.out.println(">> Erro: Completar Mensagem");
                continue;
            }

            Transport tr;
            try {
                tr = session.getTransport("smtp"); //define smtp para transporte
                tr.connect(mailSMTPServer, conexao.getAutenticacao().getUsuario(), conexao.getAutenticacao().getSenha());
                msg.saveChanges();
                tr.sendMessage(msg, msg.getAllRecipients());
                tr.close();
                sucesso = true;
            } catch (Exception e) {
                System.out.println(">> Erro: Envio Mensagem");
                continue;
            }
            break; // se chegou a esta linha o e-mail foi enviado.
        } // fim-for

        if (!sucesso) {
            throw new SendMailException("Falha no envio do e-mail");
        }
    }
}

/**
 * Classe interna para representar uma autenticação a um servidor. 
 * Herda da implementação de Autenticator da API JavaMail.
 * 
 * @author Valter Estevam
 */
class SimpleAuth extends Authenticator {

    public String username = null;
    public String password = null;

    public SimpleAuth(String user, String pwd) {
        username = user;
        password = pwd;
    }

    @Override
    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(username, password);
    }
}