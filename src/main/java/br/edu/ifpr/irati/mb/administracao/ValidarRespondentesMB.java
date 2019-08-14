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

import br.edu.ifpr.irati.dao.Dao;
import br.edu.ifpr.irati.dao.GenericDAO;
import br.edu.ifpr.irati.dao.RespondenteDAO;
import br.edu.ifpr.irati.exception.PersistenceException;
import br.edu.ifpr.irati.modelo.Campus;
import br.edu.ifpr.irati.modelo.Questionario;
import br.edu.ifpr.irati.modelo.Respondente;
import br.edu.ifpr.irati.mb.util.Mensagem;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

/**
 * 
 * Controlador da interface gráfica para validar usuários respondentes.
 * 
 * Pode ser utilizada para desconsiderar entrevistados suspeitos nos dados do relatório.
 *
 * @author Valter Estevam
 */
@ManagedBean
@SessionScoped
public class ValidarRespondentesMB {
    
    /**
     * Questionário selecionado.
     */
    private Questionario questionario;
    
    /**
     * Campus utilizado para filtrar a listagem de respondentes.
     */
    private Campus campusFiltro;
    
    /**
     * Listagem de campus.
     */
    private List<Campus> campi;
           
    /**
     * Listagem de respondentes do questionário pelo campus filtrado.
     */
    private List<Respondente> respondentes;
    
    /**
     * Objeto para acesso aos dados de respondentes.
     */
    private RespondenteDAO respondenteDAO;
    
    
    /**
     * Construtor padrão.
     */
    public ValidarRespondentesMB() {
        
        inicializar();
        
    }
    
    public void inicializar(){
        
        respondenteDAO = new RespondenteDAO();
        campusFiltro = new Campus();
        campi = new ArrayList<>();
        
        FacesContext context = FacesContext.getCurrentInstance();
        RelatorioMB relatorioMB = (RelatorioMB) context.getELContext().getELResolver().getValue(context.getELContext(), null, "relatorioMB");
        if (relatorioMB != null){
            questionario = relatorioMB.getQuestionarioSelecionado();
        }else{
            questionario = new Questionario();
        }
        Dao<Campus> campusDAO = new GenericDAO<>(Campus.class);
        try {
            campi = campusDAO.buscarTodos(Campus.class);
            if (!campi.isEmpty()){
                campusFiltro = campi.get(0);
                respondentes = respondenteDAO.buscarPorQuestionarioCampus(questionario, campusFiltro);
            }            
        } catch (PersistenceException ex) {            
            campi = new ArrayList<>();
            campusFiltro = new Campus();
        }        
        try {
            this.respondentes = respondenteDAO.buscarPorQuestionario(questionario);
        } catch (PersistenceException ex) {
            this.respondentes = new ArrayList<>();
        }                                
    }
    
    
    /**
     * Atualizar a tabela com base no novo campus selecionado.
     * 
     * @return caminho para o qual o sistema deve ser redirecionado.
     */
    public String getAtualizarTabela(){
        try {
            this.respondentes = respondenteDAO.buscarPorQuestionarioCampus(questionario, campusFiltro);
        } catch (PersistenceException ex) {
            this.respondentes = new ArrayList<>();
        }
        return "/administracao/validarrespondentes";
    }
    
    /**
     * Alterar o respondente alternando entre validado ou não validado.
     * 
     * @param respondente respondente a ser alterado.
     * @param valor true se for validado.
     * @return caminho para o qual o sistema deve ser redirecionado.
     */
    public String alterarRespondente(Respondente respondente, boolean valor) {
        Dao<Respondente> resDao = new GenericDAO<>(Respondente.class);
        try {
            respondente.setValidado(valor);
            resDao.alterar(respondente);
            String msg = "Respondente removido do relatório";
            if (respondente.isValidado()) {
                msg = "Respondente adicionado ao relatório";
            }
            Mensagem.mostrar("sucesso", "Sucesso", msg);
        } catch (PersistenceException ex) {
        }
        return "/administracao/validarrespondentes";
    }   
    
    /**
     * Retorna para a seleção de relatórios.
     * 
     * @return caminho para o qual o sistema deve ser redirecionado.
     */
    public String voltar(){
        return "/administracao/relatorio";
    }

    /**
     * 
     * @return listagem de respondentes.
     */
    public List<Respondente> getRespondentes() {
        return respondentes;
    }

    /**
     * 
     * @param respondentes listagem de respondentes.
     */
    public void setRespondentes(List<Respondente> respondentes) {
        this.respondentes = respondentes;
    }

    /**
     * 
     * @return listagem de campus.
     */
    public List<Campus> getCampi() {
        return campi;
    }

    /**
     * 
     * @param campi listagem de campus.
     */
    public void setCampi(List<Campus> campi) {
        this.campi = campi;
    }

    /**
     * 
     * @return campus selecionado para filtro.
     */
    public Campus getCampusFiltro() {
        return campusFiltro;
    }

    /**
     * 
     * @param campusFiltro campus selecionado para filtro.
     */
    public void setCampusFiltro(Campus campusFiltro) {
        this.campusFiltro = campusFiltro;
    }
        
}