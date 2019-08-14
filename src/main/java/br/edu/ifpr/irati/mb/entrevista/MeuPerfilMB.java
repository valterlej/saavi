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

import br.edu.ifpr.irati.dao.Dao;
import br.edu.ifpr.irati.dao.GenericDAO;
import br.edu.ifpr.irati.dao.QuestionarioDAO;
import br.edu.ifpr.irati.exception.HashGenerationException;
import br.edu.ifpr.irati.exception.PersistenceException;
import br.edu.ifpr.irati.mb.AcessoMB;
import br.edu.ifpr.irati.modelo.Campus;
import br.edu.ifpr.irati.modelo.Categoria;
import br.edu.ifpr.irati.modelo.Entrevistado;
import br.edu.ifpr.irati.mb.util.Digest;
import br.edu.ifpr.irati.mb.util.Mensagem;
import java.io.Serializable;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

/**
 *
 * Controlador para a interface de edição do perfil do usuário que está logado como entrevistado.
 * 
 * @author Valter Estevam
 */
@ManagedBean
@SessionScoped
public class MeuPerfilMB implements Serializable {

    /**
     * Entrevistado logado.
     */
    private Entrevistado entrevistado;

    /**
     * Campo para digitação da nova senha.
     */
    private String senha;

    /**
     * Lista de campus cadastrados no sistema.
     */
    private List<Campus> campi;
    
    /**
     * Lista de categorias cadastradas no sistema.
     */
    private List<Categoria> categorias;
    
    /**
     * Objeto para acesso aos dados do entrevistado.
     */
    private final Dao<Entrevistado> entrevistadoDAO;
    
    /**
     * Objeto para acesso aos dados de campus.
     */
    private final Dao<Campus> campusDAO;
    
    /**
     * Objeto para acesso aos dados de categoria.
     */
    private final Dao<Categoria> categoriaDAO;
    
    /**
     * Informa se é permitido alterar o campus e a categoria.
     * 
     * Isso só é permitido se o usuário ainda não respondeu a nenhum questionário.
     */
    private boolean alterarCampusCategoria = true;
    
    /**
     * Construtor padrão.
     */
    public MeuPerfilMB() {
        FacesContext context = FacesContext.getCurrentInstance();
        AcessoMB acessoMB = (AcessoMB) context.getELContext().getELResolver().getValue(context.getELContext(), null, "acessoMB");
        entrevistado = acessoMB.getEntrevistado();
        entrevistadoDAO = new GenericDAO<>(Entrevistado.class);
        campusDAO = new GenericDAO<>(Campus.class);
        categoriaDAO = new GenericDAO<>(Categoria.class);      
        try {
            campi = campusDAO.buscarTodos(Campus.class);
            categorias = categoriaDAO.buscarTodos(Categoria.class);
        } catch (PersistenceException ex) {
        } 
        //verificar se o entrevistado já respondeu algum questionario
        QuestionarioDAO questionarioDAO = new QuestionarioDAO();
        if (questionarioDAO.buscarHistoricoRespostas(entrevistado).isEmpty()){
            alterarCampusCategoria = false;
        }
        
    }

    /**
     * 
     * Salvar os dados do perfil.
     * 
     * @return caminho para o qual o sistema deve ser redirecionado.
     */
    public String salvar(){
        
        if (!senha.equals("")){
            try {
                this.entrevistado.setSenha(Digest.hashString(senha,"SHA-256"));
            } catch (HashGenerationException ex) {
            }
        }
        try {
            entrevistadoDAO.alterar(entrevistado);
            Mensagem.mostrar("sucesso","Sucesso","Perfil atualizado!");
        } catch (PersistenceException ex) {
            Mensagem.mostrar("erro","Atenção","O e-mail já se encontra em uso na base de dados.");
        }
        return "/entrevista/questionarios";
        
    }
    
    /**
     * Cancelar a edição de perfil e retornar para a tela de questionários disponíveis.
     * 
     * @return caminho para o qual o sistema deve ser redirecionado.
     */
    public String cancelar(){
        senha = "";
        return "/entrevista/questionarios";
    }
    
    
    /**
     * 
     * @return entrevistado logado.
     */
    public Entrevistado getEntrevistado() {
        return entrevistado;
    }

    /**
     * 
     * @param entrevistado entrevistado logado.
     */
    public void setEntrevistado(Entrevistado entrevistado) {
        this.entrevistado = entrevistado;
    }

    /**
     * 
     * @return senha do usuário em edição
     */
    public String getSenha() {
        return senha;
    }

    /**
     * 
     * @param senha senha do usuário em edição.
     */
    public void setSenha(String senha) {
        this.senha = senha;
    }

    /**
     *  
     * @return true se é permitido alterar o campus ou a categoria.
     */    
    public boolean isAlterarCampusCategoria() {
        return alterarCampusCategoria;
    }

    /**
     * 
     * @param alterarCampusCategoria true se é permitido alterar o campus ou a categoria.
     */
    public void setAlterarCampusCategoria(boolean alterarCampusCategoria) {
        this.alterarCampusCategoria = alterarCampusCategoria;
    }

    /**
     * 
     * @return lista de campus
     */
    public List<Campus> getCampi() {
        return campi;
    }

    /**
     * 
     * @param campi lista de campi
     */
    public void setCampi(List<Campus> campi) {
        this.campi = campi;
    }

    /**
     * 
     * @return lista de categorias.
     */
    public List<Categoria> getCategorias() {
        return categorias;
    }

    /**
     * 
     * @param categorias lista de categorias.
     */
    public void setCategorias(List<Categoria> categorias) {
        this.categorias = categorias;
    }

}