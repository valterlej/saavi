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
import br.edu.ifpr.irati.exception.PersistenceException;
import br.edu.ifpr.irati.modelo.Categoria;
import br.edu.ifpr.irati.modelo.Questao;
import br.edu.ifpr.irati.modelo.Questionario;
import br.edu.ifpr.irati.mb.util.Mensagem;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

/**
 *
 * Controlador do cadastro de questionários.
 * 
 * @author Valter Estevam
 */
@ManagedBean
@SessionScoped
public class QuestionarioMB extends CrudMB<Questionario> implements Serializable {

    /**
     * Listagem de categorias.
     */
    private List<Categoria> categorias;

    /**
     * Texto contendo os identificadores das categorias selecionadas separadas por vírgula.
     */
    private String categoriasSelecionadas;
    
    /**
     * Objeto para acesso aos dados de categorias.
     */
    private Dao<Categoria> categoriaDAO;

    /**
     * Construtor padrão.
     */
    public QuestionarioMB() {
        super(Questionario.class, "/administracao/questionario", "/administracao/administrador");
        categoriaDAO = new GenericDAO<>(Categoria.class);
        try {
            categorias = categoriaDAO.buscarTodos(Categoria.class);
        } catch (PersistenceException ex) {
            categorias = new ArrayList<>();
        }

    }

    /**
     * Editar um questionário
     * @param entidade questionário a ser editado.
     * @return caminho para o qual o sistema deve ser redirecionado.
     */
    @Override
    public String editar(Questionario entidade) {
        super.editar(entidade);
        categoriasSelecionadas = "";
        for (Categoria c : entidade.getCategorias()) {
            categoriasSelecionadas += String.valueOf(c.getIdCategoria());
            categoriasSelecionadas += ",";
        }
        if (!categoriasSelecionadas.equals("")) {
            categoriasSelecionadas = categoriasSelecionadas.substring(0, categoriasSelecionadas.length() - 1);
        }
        return view.getPaginaAtual();
    }

    /**
     * Salvar o questionário
     * 
     * @return caminho para o qual o sistema deve ser redirecionado.
     */
    @Override
    public String salvarEdicao() {

        /*
        Atualizar o conjunto de categorias com base na interface gráfica
         */
        this.entidade.getCategorias().clear();
        if (!this.categoriasSelecionadas.equals("")) {
            String ids[] = this.categoriasSelecionadas.split(",");
            for (String id : ids) {
                for (Categoria c : categorias) {
                    if (c.getIdCategoria() == Integer.parseInt(id)) {
                        entidade.getCategorias().add(c);
                    }
                }
            }
        }

        /*
        Salvar ou alterar conforme o caso.
         */
        if (view.isNovo()) {
            try {
                classeDAO.salvar(entidade);
                Mensagem.mostrar("sucesso", "Sucesso", "Registro adicionado com sucesso.");
            } catch (PersistenceException ex) {
            }
        } else {
            try {
                classeDAO.alterar(entidade);
                Mensagem.mostrar("sucesso", "Sucesso", "Registro alterado com sucesso.");
            } catch (PersistenceException ex) {
            }
        }

        /*
        Atualizar a interface gráfica
         */
        this.entidade = novaEntidade();
        try {
            entidades = classeDAO.buscarTodos(classeEntidade);
        } catch (PersistenceException ex) {
            entidades = new ArrayList<>();
        }
        view.reset();
        this.categoriasSelecionadas = "";
        return view.getPaginaAtual();
    }

    /**
     * Redireciona para a tela de gerenciamento de questões.
     * 
     * @return caminho para o qual o sistema deve ser redirecionado.
     */
    public String editarQuestoes() {

        TreeSet<Questao> questoes = new TreeSet<>(this.entidade.getQuestoes());
        this.editar(this.entidade);
        this.entidade.setQuestoes(questoes);
        FacesContext context = FacesContext.getCurrentInstance();
        QuestaoMB questaoMB = (QuestaoMB) context.getELContext().getELResolver().getValue(context.getELContext(), null, "questaoMB");
        if (questaoMB != null) {
            questaoMB.inicializar();
        }
        return "/administracao/questao";

    }

    /**
     * Retorna o identificado inteiro como string.
     * 
     * @param id identificador
     * @return identificador inteiro convertido para string.
     */
    public String idString(int id) {
        return String.valueOf(id);
    }
    
    /**
     * 
     * @return listagem de categorias.
     */
    public List<Categoria> getCategorias() {
        return categorias;
    }

    /**
     * 
     * @param categorias listagem de categorias.
     */
    public void setCategorias(List<Categoria> categorias) {
        this.categorias = categorias;
    }

    /**
     * 
     * @return categorias selecionadas pelo usuário para aplicação do questionário.
     */
    public String getCategoriasSelecionadas() {
        return categoriasSelecionadas;
    }

    /**
     * 
     * @param categoriasSelecionadas categorias selecionadas pelo usuário para aplicação do questionário.
     */
    public void setCategoriasSelecionadas(String categoriasSelecionadas) {
        this.categoriasSelecionadas = categoriasSelecionadas;
    }

}