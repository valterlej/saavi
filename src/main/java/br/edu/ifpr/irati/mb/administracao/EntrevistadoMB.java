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

import br.edu.ifpr.irati.mb.util.CrudMB;
import br.edu.ifpr.irati.dao.Dao;
import br.edu.ifpr.irati.dao.GenericDAO;
import br.edu.ifpr.irati.dao.EntrevistadoDAO;
import br.edu.ifpr.irati.exception.HashGenerationException;
import br.edu.ifpr.irati.exception.PersistenceException;
import br.edu.ifpr.irati.modelo.Campus;
import br.edu.ifpr.irati.modelo.Categoria;
import br.edu.ifpr.irati.modelo.Entrevistado;
import br.edu.ifpr.irati.mb.util.Digest;
import br.edu.ifpr.irati.mb.util.Mensagem;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * Controlador para a interface de gerenciamento de entrevistados.
 * 
 * @author Valter Estevam
 */
@ManagedBean
@SessionScoped
public class EntrevistadoMB extends CrudMB<Entrevistado> implements Serializable{

    /**
     * Relação de campus cadastrados no sistema.
     */
    private List<Campus> campi;
    
    /**
     * Relação de categorias cadastradas no sistema.
     */
    private List<Categoria> categorias;
    
    /**
     * Campus para o qual devem ser mostrados os entrevistados na tabela de listagem.
     */
    private Campus campusFiltro;
    
    /**
     * Objeto para acesso aos dados de campus.
     */
    private Dao<Campus> campusDAO;
    
    /**
     * Objeto para acesso aos dados de categoria.
     */
    private Dao<Categoria> categoriaDAO;
    
    /**
     * Objeto para acesso aos dados de entrevistados.
     */
    private EntrevistadoDAO entrevistadoDAO;
    
    /**
     * Campo para armazenamento temporário de uma nova senha para entrevistado em edição.
     */
    private String novaSenha;
    
    /**
     * Campo para armazenamento temporário de uma nova senha para entrevistado em cadastro.
     */
    @NotNull(message = "Senha é obrigatória.")
    @Size(min = 6, max = 15, message = "A senha deve possuir entre 6 e 15 caracteres.")
    private String senhaNovoEntrevistado;
    
    /**
     * Campo para armazenamento de um cpf para um novo entrevistado.
     */
    @NotNull(message = "CPF é obrigatório.")
    @Size(min = 14, max = 14, message = "Preencha o CPF.")
    private String cpfNovoEntrevistado;
    
    /**
     * Contrutor padrão.
     */
    public EntrevistadoMB() {
        super(Entrevistado.class,"/administracao/entrevistado","/administracao/administrador");
        novaSenha = "";
        campusDAO = new GenericDAO<>(Campus.class);
        categoriaDAO = new GenericDAO<>(Categoria.class); 
        entrevistadoDAO = new EntrevistadoDAO();
        //buscar todos os campus
        try {
            campi = campusDAO.buscarTodos(Campus.class);
            if (!campi.isEmpty()){
                campusFiltro = campi.get(0);
                this.entidades = entrevistadoDAO.buscarPorCampus(campusFiltro);
            }
        } catch (PersistenceException ex) {
            campi = new ArrayList<>();
            campusFiltro = new Campus();
        }
        //buscar todas as categorias
        try {
            categorias = categoriaDAO.buscarTodos(Categoria.class);
        } catch (PersistenceException ex) {
            categorias = new ArrayList<>();
        }
    }

    /**
     * Salvar ou alterar um entrevistado.
     * 
     * @return caminho para o qual o sistema deve ser redirecionado.
     */
    @Override
    public String salvarEdicao() {
        
        if (super.view.isNovo()) {
            try {                
                entidade.setCpf(Digest.hashString(cpfNovoEntrevistado,"SHA-256"));
                entidade.setSenha(Digest.hashString(senhaNovoEntrevistado,"SHA-256"));                
                entidade.setValidado(true);
                classeDAO.salvar(entidade);
                cpfNovoEntrevistado = "";
                senhaNovoEntrevistado = "";
                novaSenha = "";                
                Mensagem.mostrar("sucesso","Sucesso","Registro adicionado com sucesso.");
            } catch (PersistenceException | HashGenerationException ex) {
                Mensagem.mostrar("erro","Atenção","O CPF ou o e-mail já encontram-se em nossa base de dados.");
            }
        } else {
            try {
                if (!novaSenha.equals("")){
                    entidade.setSenha(Digest.hashString(novaSenha,"SHA-256"));
                }                
                classeDAO.alterar(entidade);
                novaSenha = "";
                Mensagem.mostrar("sucesso","Sucesso","Registro alterado com sucesso.");
            } catch (PersistenceException | HashGenerationException ex) {
            }
        }

        this.entidade = novaEntidade();
        try {
            entidades = classeDAO.buscarTodos(classeEntidade);
        } catch (PersistenceException ex) {
            entidades = new ArrayList<>();
        }
        view.reset();
        return view.getPaginaAtual();                
    }
    
    /**
     * 
     * Usado para atualizar os dados da listagem sempre que o campo de seleção de 
     * campus for alterado.
     * 
     * @return caminho para o qual o sistema deve ser redirecionado.
     */
    public String getAtualizarTabela(){
        this.entidades = entrevistadoDAO.buscarPorCampus(campusFiltro);
        return super.view.getPaginaAtual();
    }
            
    /**
     * 
     * @return relação de campus.
     */
    public List<Campus> getCampi() {
        return campi;
    }

    /**
     * 
     * @param campi relação de campus.
     */
    public void setCampi(List<Campus> campi) {
        this.campi = campi;
    }

    /**
     * 
     * @return relação de categorias.
     */
    public List<Categoria> getCategorias() {
        return categorias;
    }

    /**
     * 
     * @param categorias relação de categorias.
     */
    public void setCategorias(List<Categoria> categorias) {
        this.categorias = categorias;
    }

    /**
     * 
     * @return nova senha
     */
    public String getNovaSenha() {
        return novaSenha;
    }

    /**
     * 
     * @param novaSenha nova senha
     */
    public void setNovaSenha(String novaSenha) {
        this.novaSenha = novaSenha;
    }

    /**
     * 
     * @return senha para novo entrevistado.
     */
    public String getSenhaNovoEntrevistado() {
        return senhaNovoEntrevistado;
    }

    /**
     * 
     * @param senhaNovoEntrevistado senha para novo entrevistado.
     */
    public void setSenhaNovoEntrevistado(String senhaNovoEntrevistado) {
        this.senhaNovoEntrevistado = senhaNovoEntrevistado;
    }

    /**
     * 
     * @return CPF para novo entrevistado.
     */
    public String getCpfNovoEntrevistado() {
        return cpfNovoEntrevistado;
    }

    /**
     * 
     * @param cpfNovoEntrevistado CPF para novo entrevistado.
     */
    public void setCpfNovoEntrevistado(String cpfNovoEntrevistado) {
        this.cpfNovoEntrevistado = cpfNovoEntrevistado;
    }

    /**
     * 
     * @return campus utilizado para filtrar entrevistados.
     */
    public Campus getCampusFiltro() {
        return campusFiltro;
    }

    /**
     * 
     * @param campusFiltro campus utilizado para filtrar entrevistados.
     */
    public void setCampusFiltro(Campus campusFiltro) {
        this.campusFiltro = campusFiltro;
    }
        
}