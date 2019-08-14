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
package br.edu.ifpr.irati.mb.entrevista;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

/**
 *
 * Controlador do menu de operações da área de entrevistas. 
 * 
 * @author Valter Estevam
 */
@ManagedBean
@SessionScoped
public class EntrevistaMB implements Serializable{
        
    /**
     * Construtor padrão.
     */
    public EntrevistaMB() {
    }
       
    /**
     * Encaminha para a tela de questionários disponíveis.
     * 
     * @return caminho para a próxima página a exibir.
     */
    public String irQuestionarios(){
        return "/entrevista/questionarios";
    }
    
    /**
     * Encaminha para a tela de perfil de usuário.
     * 
     * @return caminho para a próxima página a exibir.
     */
    public String irMeuPerfil(){
        return "/entrevista/meuperfil";
    }
    
    /**
     * Encaminha para a tela de histórico de questionários respondidos.
     * 
     * @return caminho para a próxima página a exibir.
     */
    public String irHistorico(){
        return "/entrevista/historico";
    }
      
    /**
     * Encaminha para a tela de início encerrando a sessão do usuário.
     * 
     * @return caminho para a próxima página a exibir.
     */
    public String sair(){
    
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();       
        return "/index";
    }
            
}