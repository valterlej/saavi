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
import br.edu.ifpr.irati.modelo.Questionario;
import java.io.Serializable;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

/**
 * 
 * Controlador da tela de exibição de questionários disponíveis para resposta pelo usuário logado.
 *
 * @author Valter Estevam
 */
@ManagedBean
@SessionScoped
public class QuestionariosDisponiveisMB implements Serializable{
    
    /**
     * Listagem de questionários disponíveis para resposta.
     */
    private List<Questionario> questionarios;
    
    /**
     * Questionário selecionado para resposta.
     */
    private Questionario questionarioSelecionado;

    /**
     * Entrevistado logado no sistema.
     */
    private final Entrevistado entrevistado;
    
    /**
     * Objeto para acesso aos dados de questionários.
     */
    private final QuestionarioDAO questionarioDAO;
    
    /**
     * Construtor padrão.
     */    
    public QuestionariosDisponiveisMB() {
    
        FacesContext context = FacesContext.getCurrentInstance();
        AcessoMB acessoMB = (AcessoMB) context.getELContext().getELResolver().getValue(context.getELContext(), null, "acessoMB");
        entrevistado = acessoMB.getEntrevistado();
        
        questionarioDAO = new QuestionarioDAO();
        
        questionarios = questionarioDAO.buscarQuestionariosAtivos(entrevistado);
        questionarioSelecionado = new Questionario();
    
    }
    
    /**
     * Abre a tela de resposta ao questionário
     * 
     * @param questionario questionário selecionado.
     * @return caminho para o qual o sistema deve ser redirecionado. 
     */
    public String iniciarQuestionario(Questionario questionario){
        this.questionarioSelecionado = questionario;                
        
        FacesContext context = FacesContext.getCurrentInstance();
        try{
            QuestionarioEmRespostaMB questionarioEmResposta = (QuestionarioEmRespostaMB) context.getELContext().getELResolver().getValue(context.getELContext(), null, "questionarioEmRespostaMB");    
            questionarioEmResposta.inicializar();
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            externalContext.redirect(externalContext.getRequestContextPath()+ "/entrevista/questionario.xhtml");
            return "/entrevista/questionario.xhtml";
        }catch(Exception e){
            return "/entrevista/questionarios";
        }                                           
    }
        
    /**
     * Retorna em uma string se o questionário exige fornecimento de chave de acesso.
     * 
     * @param questionario questionário a ser avaliado
     * @return texto informando se Sim ou Não.
     */
    public String chaveAcesso(Questionario questionario){
        if (questionario.getChaveAcesso().equals("")){
            return "Não";
        }else{
            return "Sim";
        }
    }
    
    /**
     * 
     * @return listagem de questionários.
     */
    public List<Questionario> getQuestionarios() {
        questionarios = questionarioDAO.buscarQuestionariosAtivos(entrevistado);
        return questionarios;
    }

    /**
     * 
     * @param questionarios listagem de questionários.
     */
    public void setQuestionarios(List<Questionario> questionarios) {                
        this.questionarios = questionarios;
    }

    /**
     * 
     * @return questionário selecionado para resposta.
     */
    public Questionario getQuestionarioSelecionado() {
        return questionarioSelecionado;
    }

    /**
     * 
     * @param questionarioSelecionado questionário selecionado para resposta.
     */
    public void setQuestionarioSelecionado(Questionario questionarioSelecionado) {
        this.questionarioSelecionado = questionarioSelecionado;
    }
                    
}