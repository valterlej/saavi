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
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFileChooser;

/**
 *
 * @author Valter Estevam
 */
public class Exemplo4 {

    /**
     * Apresenta um exemplo de envio de email para mais de um destinatário e 
     * com inclusão de um ou mais arquivos ao corpo da mensagem
     * 
     * @param args 
     */
    public static void main(String[] args) {

        try {

            /* destinatários */
            List<String> destinos = new ArrayList<>();
            destinos.add("valter.junior@ifpr.edu.br");
            destinos.add("aulaemail@valterestevam.com.br");

            /* anexos escolhidos pelo usuário */
            List<File> anexos = new ArrayList<>();
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setMultiSelectionEnabled(true);            
            int retorno = fileChooser.showOpenDialog(null);
            if (retorno == JFileChooser.APPROVE_OPTION) {
                File files[] = fileChooser.getSelectedFiles();                
                for (File file: files){
                    anexos.add(file);
                }                
            } else {
                System.exit(1);
            }

            /* código básico para envio */            
            //Configuracao configuracao = new Configuracao("/home/valter/conf.xml"); //o arquivo pode estar externo à API
            Configuracao configuracao = new Configuracao();
            Email email = new Email(destinos, "Reclamação", "Ainda \nnão recebi\n meu certificado", anexos, configuracao);
            email.enviarEmail();
        } catch (SendMailException ex) {
            System.out.println(ex.getMessage());
        }
    }

}