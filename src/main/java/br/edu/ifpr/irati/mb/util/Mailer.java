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
package br.edu.ifpr.irati.mb.util;

import br.edu.ifpr.irati.configuracao.modelo.AutenticacaoEmail;
import br.edu.ifpr.irati.configuracao.modelo.PropriedadeEmail;
import br.edu.ifpr.irati.dao.Dao;
import br.edu.ifpr.irati.dao.GenericDAO;
import br.edu.ifpr.irati.exception.MailerException;
import br.edu.ifpr.irati.exception.PersistenceException;
import br.edu.ifpr.irati.jmail.configuracao.Autenticacao;
import br.edu.ifpr.irati.jmail.configuracao.Conexao;
import br.edu.ifpr.irati.jmail.configuracao.Configuracao;
import br.edu.ifpr.irati.jmail.configuracao.Propriedade;
import br.edu.ifpr.irati.jmail.exception.SendMailException;
import br.edu.ifpr.irati.jmail.mail.Email;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * Máquina para envio de e-mails em processamento paralelo.
 * 
 * @author Valter Estevam
 */
public class Mailer implements Runnable{

    /**
     * Configuração para envio de e-mail de acordo com a biblioteca jMail.
     */
    private Configuracao configuracao;
    
    /**
     * E-mail a ser encaminhado.
     */
    private Email email;

    /**
     * Construtor.
     * 
     * @param destino endereço de destino.
     * @param assunto assunto do e-mails.
     * @param texto conteúdo do e-mail.
     * @throws MailerException 
     */
    public Mailer(String destino, String assunto, String texto) throws MailerException {

        try {
            Dao<AutenticacaoEmail> autenticacaoEmailDAO = new GenericDAO<>(AutenticacaoEmail.class);
            Dao<PropriedadeEmail> propriedadeEmailDAO = new GenericDAO<>(PropriedadeEmail.class);
            AutenticacaoEmail autenticacaoEmail = autenticacaoEmailDAO.buscarPorId(1);
            List<PropriedadeEmail> propriedadeEmails = propriedadeEmailDAO.buscarTodos(PropriedadeEmail.class);
            configuracao = obterConfiguracao(propriedadeEmails, autenticacaoEmail);
            email = new Email(destino, assunto, texto, configuracao);
        } catch (PersistenceException ex) {
            throw new MailerException("Servidor de e-mails inacessível.");
        }

    }
    
    /**
     * Construtor.
     * 
     * @param destino endereço de destino.
     * @param assunto assunto do e-mails.
     * @param texto conteúdo do e-mail.
     * @param anexos relação de arquivos a serem anexados ao e-mail.
     * @throws MailerException 
     */
    public Mailer(String destino, String assunto, String texto, List<File> anexos) throws MailerException {

        try {
            Dao<AutenticacaoEmail> autenticacaoEmailDAO = new GenericDAO<>(AutenticacaoEmail.class);
            Dao<PropriedadeEmail> propriedadeEmailDAO = new GenericDAO<>(PropriedadeEmail.class);
            AutenticacaoEmail autenticacaoEmail = autenticacaoEmailDAO.buscarPorId(1);
            List<PropriedadeEmail> propriedadeEmails = propriedadeEmailDAO.buscarTodos(PropriedadeEmail.class);
            configuracao = obterConfiguracao(propriedadeEmails, autenticacaoEmail);
            email = new Email(destino, assunto, texto, anexos, configuracao);
        } catch (PersistenceException ex) {
            throw new MailerException("Servidor de e-mails inacessível.");
        }

    }
    
    /**
     * Construtor.
     * 
     * @param destinos listagem de endereços de destinatários.
     * @param assunto assunto do e-mails.
     * @param texto conteúdo do e-mail.
     * @throws MailerException 
     */
    public Mailer(List<String> destinos, String assunto, String texto) throws MailerException {

        try {
            Dao<AutenticacaoEmail> autenticacaoEmailDAO = new GenericDAO<>(AutenticacaoEmail.class);
            Dao<PropriedadeEmail> propriedadeEmailDAO = new GenericDAO<>(PropriedadeEmail.class);
            AutenticacaoEmail autenticacaoEmail = autenticacaoEmailDAO.buscarPorId(1);
            List<PropriedadeEmail> propriedadeEmails = propriedadeEmailDAO.buscarTodos(PropriedadeEmail.class);
            configuracao = obterConfiguracao(propriedadeEmails, autenticacaoEmail);            
            email = new Email(destinos, assunto, texto, configuracao);
        } catch (PersistenceException ex) {
            throw new MailerException("Servidor de e-mails inacessível.");
        }

    }
    
    /**
     * Construtor.
     * 
     * @param destinos listagem de endereços de destinatários.
     * @param assunto assunto do e-mails.
     * @param texto conteúdo do e-mail.
     * @param anexos relação de arquivos a serem anexados ao e-mail.
     * @throws MailerException 
     */
    public Mailer(List<String> destinos, String assunto, String texto, List<File> anexos) throws MailerException {

        try {
            Dao<AutenticacaoEmail> autenticacaoEmailDAO = new GenericDAO<>(AutenticacaoEmail.class);
            Dao<PropriedadeEmail> propriedadeEmailDAO = new GenericDAO<>(PropriedadeEmail.class);
            AutenticacaoEmail autenticacaoEmail = autenticacaoEmailDAO.buscarPorId(1);
            List<PropriedadeEmail> propriedadeEmails = propriedadeEmailDAO.buscarTodos(PropriedadeEmail.class);
            configuracao = obterConfiguracao(propriedadeEmails, autenticacaoEmail);            
            email = new Email(destinos, assunto, texto, anexos, configuracao);
        } catch (PersistenceException ex) {
            throw new MailerException("Servidor de e-mails inacessível.");
        }

    }
            
    /**
     * 
     * @param propriedadeEmails listagem de propriedades para envio de e-mail.
     * @param autenticacaoEnvioEmail dados para conexão à conta de e-mail de disparo automático.
     * @param destino destinatário.
     * @param assunto assunto da mensagem.
     * @param texto conteúdo da mensagem.
     * @throws MailerException 
     */
    public Mailer(List<PropriedadeEmail> propriedadeEmails, AutenticacaoEmail autenticacaoEnvioEmail, String destino, String assunto, String texto) throws MailerException {
        configuracao = obterConfiguracao(propriedadeEmails, autenticacaoEnvioEmail);
        email = new Email(destino, assunto, texto, configuracao);
    }
    
    /**
     * 
     * @param propriedadeEmails listagem de propriedades para envio de e-mail.
     * @param autenticacaoEnvioEmail dados para conexão à conta de e-mail de disparo automático.
     * @param destino destinatário.
     * @param assunto assunto da mensagem.
     * @param texto conteúdo da mensagem.
     * @param anexos relação de arquivos a serem anexados.
     * @throws MailerException 
     */
    public Mailer(List<PropriedadeEmail> propriedadeEmails, AutenticacaoEmail autenticacaoEnvioEmail, String destino, String assunto, String texto, List<File> anexos) throws MailerException {
        configuracao = obterConfiguracao(propriedadeEmails, autenticacaoEnvioEmail);
        email = new Email(destino, assunto, texto, anexos, configuracao);
    }
    
    /**
     * 
     * @param propriedadeEmails listagem de propriedades para envio de e-mail.
     * @param autenticacaoEnvioEmail dados para conexão à conta de e-mail de disparo automático.
     * @param destinos relação de endereços de destinatários da mensagem.
     * @param assunto assunto da mensagem.
     * @param texto conteúdo da mensagem.
     * @throws MailerException 
     */
    public Mailer(List<PropriedadeEmail> propriedadeEmails, AutenticacaoEmail autenticacaoEnvioEmail, List<String> destinos, String assunto, String texto) throws MailerException {
        configuracao = obterConfiguracao(propriedadeEmails, autenticacaoEnvioEmail);
        email = new Email(destinos, assunto, texto, configuracao);
    }
    
    /**
     * 
     * @param propriedadeEmails listagem de propriedades para envio de e-mail.
     * @param autenticacaoEnvioEmail dados para conexão à conta de e-mail de disparo automático.
     * @param destinos relação de destinatários da mensagem.
     * @param assunto assunto da mensagem.
     * @param texto conteúdo da mensagem.
     * @param anexos relação de arquivos a serem anexados à mensagem.
     * @throws MailerException 
     */
    public Mailer(List<PropriedadeEmail> propriedadeEmails, AutenticacaoEmail autenticacaoEnvioEmail, List<String> destinos, String assunto, String texto, List<File> anexos) throws MailerException {
        configuracao = obterConfiguracao(propriedadeEmails, autenticacaoEnvioEmail);
        email = new Email(destinos, assunto, texto, anexos, configuracao);
    }
            
    /**
     * Dada a listagem de propriedades e os dados para autenticação constói um objeto de configuração usado pela jMail.
     * 
     * @param propriedadeEmails relação de propriedades para envio de e-mails.
     * @param autenticacaoEnvioEmail dados para autenticação na conta de envio automático.
     * @return configuração em formato jMail.
     */
    private Configuracao obterConfiguracao(List<PropriedadeEmail> propriedadeEmails, AutenticacaoEmail autenticacaoEnvioEmail) {
        List<Propriedade> listaPropriedades = new ArrayList<>();
        for (PropriedadeEmail p : propriedadeEmails) {
            listaPropriedades.add(new Propriedade(p.getNome(), p.getValor()));
        }
        Autenticacao autent = new Autenticacao(autenticacaoEnvioEmail.getUsuario(), autenticacaoEnvioEmail.getSenha());
        Conexao conexao = new Conexao(listaPropriedades, autent);
        List<Conexao> conexoes = new ArrayList<>();
        conexoes.add(conexao);
        return new Configuracao(conexoes);
    }

    /**
     * Executa o Thread.
     */
    @Override
    public void run(){
        try {
            email.enviarEmail();        
        } catch (SendMailException ex) {
        }        
    }

    /**
     * Retorna o e-mail construído.
     * 
     * @return email construído.
     */
    public Email getEmail() {
        return email;
    }

    /**
     * 
     * @param email e-mail construído.
     */
    public void setEmail(Email email) {
        this.email = email;
    }

}