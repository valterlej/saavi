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
package br.edu.ifpr.irati.exception;

/**
 *
 * Exceção disparada quando ocorre uma falha no envio de um e-mail a partir do sistema.
 * 
 * @author Valter Estevam
 */
public class MailerException extends Exception{

    /**
     * 
     * @param mensagem mensagem a ser disparada.
     */
    public MailerException(String mensagem) {
        super(mensagem);
    }
    
}