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

import br.edu.ifpr.irati.jmail.configuracao.Configuracao;
import br.edu.ifpr.irati.jmail.exception.SendMailException;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * Representa um e-mail a ser encaminhado pela API
 * 
 * @author Valter Estevam
 */
public class Email {

    /**
     * Relação de endereços destinatários.
     */
    private List<String> destinos;
    
    /**
     * Assunto do e-mail.
     */
    private String assunto;
    
    /**
     * Conteúdo textual da mensagem.
     */
    private String texto;
    
    /**
     * Configuração para envio de e-mails.
     */
    private Configuracao configuracao;
    
    /**
     * Relação de arquivos anexos à mensagem
     */
    private List<File> anexos;

    /**
     * Construtor para envio de e-mail para destinatário único e sem anexos.
     * 
     * @param destino
     * @param assunto
     * @param texto
     * @param configuracao 
     */
    public Email(String destino, String assunto, String texto, Configuracao configuracao) {
        this.destinos = new ArrayList<>();
        this.destinos.add(destino);
        this.assunto = assunto;
        this.texto = montarMensagem(texto);
        this.configuracao = configuracao;
        this.anexos = new ArrayList<>();
    }
    
    /**
     * Construtor para envio de e-mail para destinatário único com anexos.
     * 
     * @param destino
     * @param assunto
     * @param texto
     * @param anexos
     * @param configuracao 
     */
    public Email(String destino, String assunto, String texto, List<File> anexos, Configuracao configuracao) {
        this.destinos = new ArrayList<>();
        this.destinos.add(destino);
        this.assunto = assunto;
        this.texto = montarMensagem(texto);
        this.configuracao = configuracao;
        this.anexos = anexos;
    }

    /**
     * Construtor para envio de e-mail para vários destinatários com anexos.
     * 
     * @param destinos
     * @param assunto
     * @param texto
     * @param configuracao 
     */
    public Email(List<String> destinos, String assunto, String texto, Configuracao configuracao) {
        this.destinos = destinos;
        this.assunto = assunto;
        this.texto = montarMensagem(texto);
        this.configuracao = configuracao;
        this.anexos = new ArrayList<>();
    }
    
    /**
     * Construtor para envio de e-mail a vários destinatários e com arquivos em anexo.
     * 
     * @param destinos
     * @param assunto
     * @param texto
     * @param anexos
     * @param configuracao 
     */
    public Email(List<String> destinos, String assunto, String texto, List<File> anexos, Configuracao configuracao) {
        this.destinos = destinos;
        this.assunto = assunto;
        this.texto = montarMensagem(texto);
        this.configuracao = configuracao;
        this.anexos = anexos;
    }

    /**
     * Método para disparar o e-mail
     * 
     * @throws SendMailException 
     */
    public void enviarEmail() throws SendMailException {
        SendMail sendMail = new SendMail(configuracao);
        sendMail.sendMail(this);
    }

    /**
     * Monta uma mensagem e a formata para o padrão HTML.
     *
     * @param texto
     * @return texto em formato html
     */
    public String montarMensagem(String texto) {

        StringBuilder sb = new StringBuilder();
        if (texto.contains("\n")) {
            String textos[] = texto.split("\n");
            for (String s : textos) {
                sb.append("<p>");
                sb.append(s);
                sb.append("</p>");
            }
        } else {
            sb.append("<p>");
            sb.append(texto);
            sb.append("</p>");
        }

        String msg
                = "<html>"
                + "<head>"
                + "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">"
                + "</head>"
                + "<body>"
                + sb.toString()
                + "</body>"
                + "</html>";
        return msg;
    }

    /**
     * @return the destinos
     */
    public List<String> getDestinos() {
        return destinos;
    }

    /**
     * @param destinos the destino to set
     */
    public void setDestinos(List<String> destinos) {
        this.destinos = destinos;
    }

    /**
     * @return the assunto
     */
    public String getAssunto() {
        return assunto;
    }

    /**
     * @param assunto the assunto to set
     */
    public void setAssunto(String assunto) {
        this.assunto = assunto;
    }

    /**
     * @return the texto
     */
    public String getTexto() {
        return texto;
    }

    /**
     * @param texto the texto to set
     */
    public void setTexto(String texto) {
        this.texto = texto;
    }

    /**
     * @return the configuracao
     */
    public Configuracao getConfiguracao() {
        return configuracao;
    }

    /**
     * @param configuracao the configuracao to set
     */
    public void setConfiguracao(Configuracao configuracao) {
        this.configuracao = configuracao;
    }

    /**
     * @return the anexos
     */
    public List<File> getAnexos() {
        return anexos;
    }

    /**
     * @param anexos the anexos to set
     */
    public void setAnexos(List<File> anexos) {
        this.anexos = anexos;
    }
}