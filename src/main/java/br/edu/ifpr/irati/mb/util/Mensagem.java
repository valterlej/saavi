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

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;

/**
 * Representa uma mensagem exibida em um growl na interface gráfica.
 *
 * @author Valter Estevam
 */
public class Mensagem {
    
    /**
     * Retorna um tipo de severity a partir de um string
     * @param severity texto de severidade que pode ser: "sucesso", "erro" ou "alerta".
     * @return severity
     */
    private static Severity getSeverity(String severity){
        
        if (severity.equals("sucesso")){
            return FacesMessage.SEVERITY_INFO;
        }else if (severity.equals("erro")){
            return FacesMessage.SEVERITY_ERROR;           
        }else if (severity.equals("alerta")){
            return FacesMessage.SEVERITY_WARN;
        }else{
            return FacesMessage.SEVERITY_INFO;
        }
        
    }
        
    /**
     * Exibir uma mensagem em tela dentro de um elemento growl.
     * 
     * @param tipo sucesso, erro ou alerta
     * @param titulo
     * @param mensagem 
     */
    public static void mostrar(String tipo, String titulo, String mensagem){
        FacesContext context = FacesContext.getCurrentInstance();         
        context.addMessage(null, new FacesMessage(Mensagem.getSeverity(tipo),titulo,mensagem)); 
    }
    
}