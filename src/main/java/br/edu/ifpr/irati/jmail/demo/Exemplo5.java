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
package br.edu.ifpr.irati.jmail.demo;

import br.edu.ifpr.irati.jmail.configuracao.Autenticacao;
import br.edu.ifpr.irati.jmail.configuracao.Conexao;
import br.edu.ifpr.irati.jmail.configuracao.Configuracao;
import br.edu.ifpr.irati.jmail.configuracao.Propriedade;
import br.edu.ifpr.irati.jmail.exception.SendMailException;
import br.edu.ifpr.irati.jmail.mail.Email;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Valter Estevam
 */
public class Exemplo5 {

    public static Configuracao obterConfiguracaoDefault(){
        List<Propriedade> listaPropriedades = new ArrayList<>();
        listaPropriedades.add(new Propriedade("mail.transport.protocol", "smtp"));
        listaPropriedades.add(new Propriedade("mail.smtp.starttls.enable", "true"));
        listaPropriedades.add(new Propriedade("mail.smtp.host", "mail.valterestevam.com.br"));
        listaPropriedades.add(new Propriedade("mail.smtp.auth", "true"));
        listaPropriedades.add(new Propriedade("mail.smtp.user", "saavidemo@valterestevam.com.br"));
        listaPropriedades.add(new Propriedade("mail.debug", "true"));
        listaPropriedades.add(new Propriedade("mail.smtp.port", "587"));
        //listaPropriedades.add(new Propriedade("proxySet", "true"));
        //listaPropriedades.add(new Propriedade("socksProxyHost", "192.168.155.1"));
        //listaPropriedades.add(new Propriedade("socksProxyPort", "1080"));
        //listaPropriedades.add(new Propriedade("mail.smtp.socketFactory.port", "587"));
        //listaPropriedades.add(new Propriedade("mail.smtp.socketFactory.fallback", "false"));                
        Autenticacao autent = new Autenticacao("saavidemo@valterestevam.com.br", "saaviifpr2019");
        Conexao conexao = new Conexao(listaPropriedades, autent);
        List<Conexao> conexoes = new ArrayList<>();
        conexoes.add(conexao);

        return new Configuracao(conexoes);
    }
    
    
    public static void main(String[] args) {
        
        Email email = new Email("valter.junior@ifpr.edu.br", "Reclamação", "Ainda \n não recebi\n meu certificado", Exemplo5.obterConfiguracaoDefault()); // o caractere \n é substituído por <p> </p> na conversão para html
        try {
            email.enviarEmail();
        } catch (SendMailException ex) {
            System.out.println(ex.getMessage());
        }

    }

}