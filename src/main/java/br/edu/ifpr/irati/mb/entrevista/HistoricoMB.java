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

import br.edu.ifpr.irati.dao.QuestionarioDAO;
import br.edu.ifpr.irati.mb.AcessoMB;
import br.edu.ifpr.irati.modelo.Entrevistado;
import br.edu.ifpr.irati.modelo.Respondente;
import java.io.Serializable;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

/**
 *
 * Controlador para a interface de exibição de histórico de participações como entrevistado.
 * 
 * @author Valter Estevam
 */
@ManagedBean
@ViewScoped
public class HistoricoMB implements Serializable {
    
    /**
     * Listagem de respostas a questionários anteriores.
     */
    private List<Respondente> respostas;   
    
    /**
     * Construtor padrão.
     */
    public HistoricoMB() {    
        FacesContext context = FacesContext.getCurrentInstance();
        AcessoMB acessoMB = (AcessoMB) context.getELContext().getELResolver().getValue(context.getELContext(), null, "acessoMB");
        Entrevistado entrevistado = acessoMB.getEntrevistado();        
        QuestionarioDAO questionarioDAO = new QuestionarioDAO();
        this.respostas = questionarioDAO.buscarHistoricoRespostas(entrevistado);    
    }
    
    /**
     * Cancela a visualização do histórico.
     * 
     * @return caminho para o qual o sistema deve ser redirecionado.
     */
    public String voltar(){
        return "/entrevista/questionarios";
    }
        
    /**
     * 
     * @return listagem de respostas a questionários anteriores.
     */
    public List<Respondente> getRespostas() {
        return respostas;
    }

    /**
     * 
     * @param respostas listagem de respostas a questionários anteriores.
     */
    public void setRespostas(List<Respondente> respostas) {
        this.respostas = respostas;
    }
            
}