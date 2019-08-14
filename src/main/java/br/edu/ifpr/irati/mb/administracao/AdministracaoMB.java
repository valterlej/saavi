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
package br.edu.ifpr.irati.mb.administracao;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

/**
 *
 * Controlador do menu de operações da área de administrador.
 * 
 * @author Valter Estevam
 */
@ManagedBean
@SessionScoped
public class AdministracaoMB implements Serializable{

    /**
     * Construtor padrão.
     */
    public AdministracaoMB() {
    }
              
    /**
     * Encaminha para a tela de administradores.
     * 
     * @return caminho para a próxima página a exibir.
     */
    public String irAdministrador(){
        return "/administracao/administrador";
    }
    
    /**
     * Encaminha para a tela de configurações.
     * 
     * @return caminho para a próxima página a exibir.
     */
    public String irConfiguracao(){
        return "/administracao/configuracao";
    }
    
    /**
     * Encaminha para a tela de campus.
     * 
     * @return caminho para a próxima página a exibir.
     */
    public String irCampus(){
        return "/administracao/campus";
    }
    
    /**
     * Encaminha para a tela de categorias.
     * 
     * @return caminho para a próxima página a exibir.
     */
    public String irCategoria(){
        return "/administracao/categoria";
    }

    /**
     * Encaminha para a tela de entrevistados.
     * 
     * @return caminho para a próxima página a exibir.
     */
    public String irEntrevistado(){
        return "/administracao/entrevistado";
    }
    
    /**
     * Encaminha para a tela de questionários.
     * 
     * @return caminho para a próxima página a exibir.
     */
    public String irQuestionario(){
        return "/administracao/questionario";
    }
    
    /**
     * Encaminha para a tela de relatórios.
     * 
     * @return caminho para a próxima página a exibir.
     */
    public String irRelatorio(){
        return "/administracao/relatorio";
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