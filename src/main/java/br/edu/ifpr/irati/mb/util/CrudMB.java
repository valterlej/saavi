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

import br.edu.ifpr.irati.dao.Dao;
import br.edu.ifpr.irati.dao.GenericDAO;
import br.edu.ifpr.irati.exception.PersistenceException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * Classe abstrata que contém atributos e métodos necessários para o gerenciamento default de entidades do sistema como, 
 * por exemplo: Campus, Categorias, Administradores, Entrevistados...
 * 
 * @author Valter Estevam
 * @param <T>
 */
public abstract class CrudMB<T> {
    
    /**
     * Lista de entidades
     */
    protected List<T> entidades;

    /**
     * Classe para a qual o controlador está sendo criado.
     */
    protected final Class classeEntidade;
    
    /**
     * Entidade em cadastro ou em edição.
     */
    protected T entidade;

    /**
     * Objeto genérico para acesso ao banco de dados.
     */
    protected Dao<T> classeDAO;

    /**
     * Parâmetros utilizados para definir quais elementos devem ser mostrados ou ocultados na interface gráfica.
     */
    protected ParametrosViewCrud view;
    

    /**
     * Construtor que deve ser executado sempre que um controlador implementador desta classe for criado.
     * 
     * @param classeEntidade classe para a qual o controlador foi criado.
     * @param paginaAtual página xhtml que utiliza o controlador
     * @param homePage página para a qual o sistema deve retornar caso o cancelar seja clicado.
     */
    protected CrudMB(Class classeEntidade, String paginaAtual, String homePage) {
        
        this.classeEntidade = classeEntidade;
        
        classeDAO = new GenericDAO<>(classeEntidade);
        try {
            entidades = classeDAO.buscarTodos(classeEntidade);
            
            
        } catch (PersistenceException ex) {
            entidades = new ArrayList<>();
        }
        entidade = novaEntidade();
        view = new ParametrosViewCrud(paginaAtual,homePage);
    }
    
    /**
     * Instancia uma nova entidade
     * 
     * @return objeto da nova entidade
     */
    public T novaEntidade(){
        return (T) EntidadeFactory.getObject(classeEntidade);
    }

    /*
    Comandos do painel de listagem
     */
    
    /**
     * Criar uma nova entidade.
     * 
     * @return página para a qual o sistema deve ser redirecionado.
     */
    public String novo() {
        entidade = novaEntidade();
        view.modoInserir();
        return view.getPaginaAtual();
    }

    /**
     * Coloca a interface em modo de edição e define a entidade atual como 
     * sendo aquela dada por parâmetro
     * 
     * @param entidade entidade a ser editada.
     * @return página para a qual o sistema deve ser redirecionado.
     */
    public String editar(T entidade) {
        this.entidade = entidade;
        view.modoAlterar();
        return view.getPaginaAtual();
    }

    /**
     * Remove uma entidade do cadastro.
     * 
     * @param entidade entidade a ser removida.
     * @return página para a qual o sistema deve ser redirecionado.
     */
    public String remover(T entidade) {
                                
        try {
            classeDAO.excluir(entidade);
            Mensagem.mostrar("sucesso","Sucesso","Registro excluído com sucesso.");
        } catch (PersistenceException ex) {
            Mensagem.mostrar("erro","Erro", "Não foi possível fazer a exclusão porque há cadastros associados.");
        }

        //atualizar a listagem
        try {
            entidades = classeDAO.buscarTodos(classeEntidade);
        } catch (PersistenceException ex) {
            entidades = new ArrayList<>();
        }
        
        view.reset();        
        return view.getPaginaAtual();
    }

    /**
     * Cancela o gerenciamento da entidade retornando para a home page.
     * @return caminho para o qual o sistema deve retornar. Neste caso para a home page.
     */
    public String cancelar() {
        entidade = novaEntidade();
        
        view.reset();
        return view.getHomePage();
    }

    /*
    Comandos do painel de edição
     */
    
    /**
     * Atualiza ou cria um registro no banco de dados
     * @return caminho para navegação após o cadastro
     */
    public String salvarEdicao() {

        if (view.isNovo()) {
            try {
                classeDAO.salvar(entidade);
                Mensagem.mostrar("sucesso","Sucesso","Registro adicionado com sucesso.");
            } catch (PersistenceException ex) {
            }
        } else {
            try {
                classeDAO.alterar(entidade);
                Mensagem.mostrar("sucesso","Sucesso","Registro alterado com sucesso.");
            } catch (PersistenceException ex) {
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
     * Cancelar a edição de uma entidade devolvendo a tela para a listagem de entidades.
     * @return caminho para o qual o sistema deve ser redirecionado.
     */
    public String cancelarEdicao() {

        this.entidade = novaEntidade();
        view.reset();
        return view.getPaginaAtual();
    }

    /**
     * 
     * @return listagem de entidades.
     */
    public List<T> getEntidades() {
        return entidades;
    }

    /**
     * 
     * @param entidades listagem de entidades.
     */
    public void setEntidades(List<T> entidades) {
        this.entidades = entidades;
    }

    
    /**
     * 
     * @return parâmetros de controle dos elementos da interface gráfica.
     */
    public ParametrosViewCrud getView() {
        return view;
    }

    /**
     * 
     * @param view parâmetros de controle dos elementos da interface gráfica.
     */
    public void setView(ParametrosViewCrud view) {
        this.view = view;
    }

    /**
     * 
     * @return entidade em cadastro ou edição.
     */
    public T getEntidade() {
        return entidade;
    }

    /**
     * 
     * @param entidade entidade em cadastro ou edição.
     */
    public void setEntidade(T entidade) {
        this.entidade = entidade;
    }

}