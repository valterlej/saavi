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

import br.edu.ifpr.irati.jmail.configuracao.Configuracao;
import br.edu.ifpr.irati.jmail.mail.Email;
import br.edu.ifpr.irati.jmail.exception.SendMailException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Valter Estevam
 */
public class Exemplo2 {

    /**
     * Envio de email para dois destinatários sem anexos no corpo da 
     * mensagem
     * 
     * @param args 
     */
    public static void main(String[] args) {

        try {
            
            /* destinatários */
            List<String> destinos = new ArrayList<>();
            destinos.add("valter.junior@ifpr.edu.br");
            destinos.add("aulaemail@valterestevam.com.br");
            
            /* código básico para envio */
            Configuracao configuracao = new Configuracao();
            Email email = new Email(destinos, "Reclamação", "Ainda \nnão recebi\n meu certificado", configuracao);
            email.enviarEmail();
        } catch (SendMailException ex) {
            System.out.println(ex.getMessage());
        }      
    }

}