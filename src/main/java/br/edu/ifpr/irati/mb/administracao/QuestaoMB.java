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
import br.edu.ifpr.irati.modelo.Opcao;
import br.edu.ifpr.irati.modelo.Questao;
import br.edu.ifpr.irati.modelo.QuestaoAberta;
import br.edu.ifpr.irati.modelo.QuestaoMultiplaEscolha;
import br.edu.ifpr.irati.modelo.Questionario;
import br.edu.ifpr.irati.mb.util.Mensagem;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

/**
 *
 * Controlador do cadastro de questões.
 * 
 * @author Valter Estevam
 */
@ManagedBean
@SessionScoped
public class QuestaoMB extends CrudMB<Questao> implements Serializable {

    /**
     * Questionário em edição.
     */
    private Questionario questionario; //referência local ao questionário que está na sessão do usuário
    
    /**
     * Listagem de opções em edição.
     */
    private List<Opcao> opcoes;

    /**
     * Informa se é uma questão aberta.
     */
    private boolean aberta;
    
    /**
     * Informa se é uma questão de múltipla escolha.
     */
    private boolean multiplaEscolha;
    
    /**
     * Objeto para acesso aos dados de questionário.
     */
    private Dao<Questionario> questionarioDAO;
    
    /**
     * Objeto para acesso aos dados de questão.
     */
    private Dao<Questao> questaoDAO;
    
    /**
     * Objeto para acesso aos dados de questão aberta.
     */
    private Dao<QuestaoAberta> questaoAbertaDAO;
    
    /**
     * Objeto para acesso aos dados de questão de múltipla escolha.
     */
    private Dao<QuestaoMultiplaEscolha> questaoMultiplaEscolhaDAO;
    
    /**
     * Objeto para acesso aos dados de opção.
     */
    private Dao<Opcao> opcaoDAO;

    
    /**
     * Construtor padrão.
     */
    public QuestaoMB() {
        super(Questao.class, "/administracao/questao", "/administracao/questionario");
        inicializar();        
    }
    
    /**
     * Inicializa a interface com os dados necessário buscando em outro controlador.
     */
    public void inicializar(){
        //instancia dos objetos de acesso aos dados
        questionarioDAO = new GenericDAO<>(Questionario.class);
        questaoDAO = new GenericDAO<>(Questao.class);
        questaoAbertaDAO = new GenericDAO<>(QuestaoAberta.class);
        questaoMultiplaEscolhaDAO = new GenericDAO<>(QuestaoMultiplaEscolha.class);
        opcaoDAO = new GenericDAO<>(Opcao.class);

        //obter o questionário ao qual as questões estão vinculadas
        FacesContext context = FacesContext.getCurrentInstance();
        QuestionarioMB questionarioMB = (QuestionarioMB) context.getELContext().getELResolver().getValue(context.getELContext(), null, "questionarioMB");
        this.questionario = questionarioMB.getEntidade();

        //atribuir valor à lista de entidades e à entidade atual
        this.entidades = new ArrayList<>(this.questionario.getQuestoes());
        Collections.sort(entidades);
        this.entidade = novaEntidade();

        //inicializar os paineis de edição como falso (não mostrar)
        aberta = false;
        multiplaEscolha = false;

        //inicializar a lista de opções
        opcoes = new ArrayList<>();
    }

    /**
     * Altera a ordem de uma questão trocando com a questão acima.
     * 
     * @param questao questão a ser alterada a ordem.
     * @return caminho para o qual o sistema deve ser redirecionado.
     */
    public String subir(Questao questao) {

        if (questao.getOrdem() == 1) {
            Mensagem.mostrar("alerta", "Atenção", "Esta questão já é a primeira a ser apresentada ao entrevistado.");
            return view.getPaginaAtual();
        } else {
            for (int i = 0; i < this.entidades.size(); i++) {
                if (this.entidades.get(i).getOrdem() == questao.getOrdem() - 1) {
                    this.entidades.get(i).setOrdem(questao.getOrdem());
                    questao.setOrdem(questao.getOrdem() - 1);
                    try {
                        questaoDAO.alterar(this.entidades.get(i));
                        questaoDAO.alterar(questao);
                        Collections.sort(entidades);
                    } catch (PersistenceException ex) {
                    }
                    break;
                }
            }
        }
        return view.getPaginaAtual();
    }

    /**
     * Altera a ordem de uma questão trocando com a questão abaixo.
     * @param questao
     * @return caminho para o qual o sistema deve ser redirecionado.
     */
    public String descer(Questao questao) {

        if (questao.getOrdem() == this.entidades.size()) {
            Mensagem.mostrar("alerta", "Atenção", "Esta questão já é a última a ser apresentada ao entrevistado.");
            return view.getPaginaAtual();
        } else {
            for (int i = 0; i < this.entidades.size(); i++) {
                if (this.entidades.get(i).getOrdem() == questao.getOrdem() + 1) {
                    this.entidades.get(i).setOrdem(questao.getOrdem());
                    questao.setOrdem(questao.getOrdem() + 1);
                    try {
                        questaoDAO.alterar(this.entidades.get(i));
                        questaoDAO.alterar(questao);
                        Collections.sort(entidades);
                    } catch (PersistenceException ex) {
                    }
                    break;
                }
            }
        }
        return view.getPaginaAtual();
    }

    /**
     * Remove uma questão da listagem de questões do questionário.
     * @param entidade questão a ser removida.
     * @return caminho para o qual o sistema deve ser redirecionado.
     */
    @Override
    public String remover(Questao entidade) {

        try {

            //atualizar a relação de questionário e questão e excluir a questão
            this.questionario.getQuestoes().remove(entidade);
            questionarioDAO.alterar(questionario);
            questaoDAO.excluir(entidade);

            //atualizar a ordem das questoes.
            this.entidades = new ArrayList<>(this.questionario.getQuestoes());
            Collections.sort(entidades);

            int o = 1; //ordem
            for (Questao q : this.entidades) {
                q.setOrdem(o++);
                questaoDAO.alterar(q);
            }

            return view.getPaginaAtual();
        } catch (PersistenceException ex) {
            Mensagem.mostrar("erro", "Atenção", "Esta questão não pode ser removida porque há registros associados.");
            return view.getPaginaAtual();
        }
    }

    /**
     * Chamado pela interface gráfica para indicar que um painel para uma nova questão aberta
     * deve ser exibido.
     *
     * @return caminho para o qual o sistema deve ser redirecionado.
     */
    public String novaAberta() {
        view.modoInserir();
        entidade = new QuestaoAberta();
        aberta = true;
        multiplaEscolha = false;
        return view.getPaginaAtual();
    }

    /**
     * Chamado pela interface gráfica para indicar que um painel para uma nova questão de múltipla escolha
     * deve ser exibido.
     * 
     * @return caminho para o qual o sistema deve ser redirecionado.
     */
    public String novaMultiplaEscolha() {
        view.modoInserir();
        entidade = new QuestaoMultiplaEscolha();
        opcoes = new ArrayList<>();
        aberta = false;
        multiplaEscolha = true;
        return view.getPaginaAtual();
    }

    /**
     *
     * Cadastra ou altera a questão atual.
     * 
     * @return caminho para o qual o sistema deve ser redirecionado.
     */
    @Override
    public String salvarEdicao() {

        String textoMensagem = "";

        if (super.view.isNovo()) { // adicionar questão
            //obter a ordem da nova questão
            int pos = 1;
            if (!this.entidades.isEmpty()) {
                pos = this.entidades.get(this.entidades.size() - 1).getOrdem() + 1;
            }

            //vincular o questionário à entidade
            entidade.setQuestionario(questionario);
            //atribuir a ordem à nova entidade
            entidade.setOrdem(pos);

            boolean flag = true;

            if (entidade instanceof QuestaoAberta) {
                try {
                    //salvar a nova questão ao banco de dados
                    questaoAbertaDAO.salvar((QuestaoAberta) entidade);
                } catch (PersistenceException ex) {
                    flag = false;
                }
            } else if (entidade instanceof QuestaoMultiplaEscolha) {
                try {
                    //salvar todas as opções
                    for (Opcao o : opcoes) {
                        opcaoDAO.salvar(o);
                        ((QuestaoMultiplaEscolha) entidade).getOpcoes().add(o);
                    }
                    questaoMultiplaEscolhaDAO.salvar((QuestaoMultiplaEscolha) entidade);
                } catch (PersistenceException ex) {
                    flag = false;
                }
            }

            if (flag) {
                //adicionar a nova questão ao questionário (fazer o relacionamento entre as entidades Questionário e Questão)
                this.questionario.getQuestoes().add(entidade);
                try {
                    questionarioDAO.alterar(questionario);
                } catch (PersistenceException ex) {
                    this.questionario.getQuestoes().remove(entidade);
                }
            }

            //atualizar a lista de entidades
            this.entidades = new ArrayList<>(this.questionario.getQuestoes());
            Collections.sort(entidades);

            textoMensagem = "Questão adicionada com sucesso.";

        } else { //edição de registro existente.

            if (entidade instanceof QuestaoAberta) {
                try {
                    //salvar a nova questão ao banco de dados
                    questaoAbertaDAO.alterar((QuestaoAberta) entidade);
                } catch (PersistenceException ex) {
                }
            } else if (entidade instanceof QuestaoMultiplaEscolha) {

                //atualizar ou adicionar opção ao banco de dados
                try {
                    for (Opcao o : opcoes) {
                        if (o.getIdOpcao() == 0) {
                            opcaoDAO.salvar(o);
                        } else {
                            opcaoDAO.alterar(o);
                        }
                    }
                } catch (PersistenceException ex) {
                }

                QuestaoMultiplaEscolha qm = (QuestaoMultiplaEscolha) entidade;
                qm.getOpcoes().clear();
                for (Opcao o : opcoes) {
                    qm.getOpcoes().add(o);
                }
                try {
                    questaoMultiplaEscolhaDAO.alterar((QuestaoMultiplaEscolha) entidade);
                } catch (PersistenceException ex) {
                }
            }

            textoMensagem = "Questão alterada com sucesso.";
        }

        this.entidade = novaEntidade();
        view.reset();
        aberta = false;
        multiplaEscolha = false;
        Mensagem.mostrar("sucesso", "Sucesso", textoMensagem);
        return view.getPaginaAtual();

    }

    /**
     * Cancela a edição da questão.
     * 
     * @return caminho para o qual o sistema deve ser redirecionado.
     */
    @Override
    public String cancelarEdicao() {
        super.cancelarEdicao();
        entidade = novaEntidade();
        aberta = false;
        multiplaEscolha = false;
        opcoes = new ArrayList<>();
        return view.getPaginaAtual();
    }

    /**
     * Chamado pela interface gráfica para indicar qual painel deve ser aberto
     * em modo de alteração
     *
     * @param entidade questão a ser editada.
     * @return caminho para o qual o sistema deve ser redirecionado.
     */
    @Override
    public String editar(Questao entidade) {
        super.editar(entidade);
        if (entidade instanceof QuestaoAberta) {
            aberta = true;
            multiplaEscolha = false;
        } else { // múltipla escolha
            opcoes = new ArrayList<>(((QuestaoMultiplaEscolha) entidade).getOpcoes());
            Collections.sort(opcoes);
            aberta = false;
            multiplaEscolha = true;
        }
        return view.getPaginaAtual();
    }

    /**
     * Adiciona uma opção a uma questão de múltipla escolha.
     * 
     * @return caminho para o qual o sistema deve ser redirecionado.
     */
    public String adicionarOpcao() {
        opcoes.add(new Opcao());
        return view.getPaginaAtual();
    }

    /**
     * Remove uma opção de uma questão de múltipla escolha.
     * 
     * @param opcao opção a ser removida.
     * @return caminho para o qual o sistema deve ser redirecionado.
     */
    public String removerOpcao(Opcao opcao) {

        QuestaoMultiplaEscolha qm = (QuestaoMultiplaEscolha) entidade;

        if (qm.getIdQuestao() == 0) {//nova questão
            opcoes.remove(opcao);
            return view.getPaginaAtual();
        } else {//editando questão existente
            //remover objeto do relacionamento
            qm.getOpcoes().remove(opcao);
            try {
                //excluir relacionamento do banco de dados
                questaoMultiplaEscolhaDAO.alterar(qm);

                //excluir opção do banco de dados
                opcaoDAO.excluir(opcao);
                opcoes.remove(opcao);
                Mensagem.mostrar("sucesso","Sucesso","Opção excluída com sucesso.");
                return view.getPaginaAtual();
            } catch (PersistenceException ex) {
                return view.getPaginaAtual();
            }
        }

    }

    /**
     * 
     * @return true se a questão atual é aberta.
     */
    public boolean isAberta() {
        return aberta;
    }

    /**
     * 
     * @param aberta true se a questão atual é aberta.
     */
    public void setAberta(boolean aberta) {
        this.aberta = aberta;
    }

    /**
     * 
     * @return true se a questão atual é de múltipla escolha.
     */
    public boolean isMultiplaEscolha() {
        return multiplaEscolha;
    }

    /**
     * 
     * @param multiplaEscolha true se a questão atual é de múltipla escolha.
     */
    public void setMultiplaEscolha(boolean multiplaEscolha) {
        this.multiplaEscolha = multiplaEscolha;
    }

    /**
     * 
     * @return listagem de opções.
     */
    public List<Opcao> getOpcoes() {
        return opcoes;
    }

    /**
     * 
     * @param opcoes listagem de opções.
     */
    public void setOpcoes(List<Opcao> opcoes) {
        this.opcoes = opcoes;
    }

}