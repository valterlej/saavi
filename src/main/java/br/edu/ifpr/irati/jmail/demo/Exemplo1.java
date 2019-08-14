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

/**
 *
 * @author Valter Estevam
 */
public class Exemplo1 {
    
    /**
     * Envio de email para um único destinatário e sem inclusão de anexos 
     * ao corpo da mensagem
     * 
     * @param args 
     */
    public static void main(String[] args) {
        
        try {
            
            /* código básico para envio */
            Configuracao configuracao = new Configuracao();                                                
            Email email = new Email("valter.junior@ifpr.edu.br","Reclamação","Ainda \n não recebi\n meu certificado", configuracao); // o caractere \n é substituído por <p> </p> na conversão para html
            email.enviarEmail();
        } catch (SendMailException ex) {
            System.out.println(ex.getMessage());
        }
        
    }
    
}