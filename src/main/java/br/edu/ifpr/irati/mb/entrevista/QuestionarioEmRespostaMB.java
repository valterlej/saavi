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
import br.edu.ifpr.irati.exception.PersistenceException;
import br.edu.ifpr.irati.mb.AcessoMB;
import br.edu.ifpr.irati.modelo.Entrevistado;
import br.edu.ifpr.irati.modelo.Opcao;
import br.edu.ifpr.irati.modelo.OpcaoResposta;
import br.edu.ifpr.irati.modelo.Questao;
import br.edu.ifpr.irati.modelo.QuestaoAberta;
import br.edu.ifpr.irati.modelo.QuestaoMultiplaEscolha;
import br.edu.ifpr.irati.modelo.Questionario;
import br.edu.ifpr.irati.modelo.Respondente;
import br.edu.ifpr.irati.modelo.Resposta;
import br.edu.ifpr.irati.modelo.RespostaAberta;
import br.edu.ifpr.irati.modelo.RespostaMultiplaEscolha;
import br.edu.ifpr.irati.mb.util.Mensagem;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

/**
 *
 * Controlador para o processo de entrevista.
 * 
 * @author Valter Estevam
 */
@ManagedBean
@SessionScoped
public class QuestionarioEmRespostaMB implements Serializable {

    /**
     * Usuário que está sendo entrevistado.
     */
    private Entrevistado entrevistado;

    /**
     * Questionário em resposta.
     */
    private Questionario questionario;

    /**
     * Informa se a chave de acesso foi liberada.
     */
    private boolean chaveAcessoLiberada;

    /**
     * Guarda a chave que foi digitada pelo usuário.
     */
    private String chaveDigitada;

    /**
     * Mapa para as questões.
     */
    private Map<Integer, Questao> questoes;
    
    /**
     * Mapa para as respostas.
     */
    private Map<Integer, Resposta> respostas;
    
    /**
     * Listagem com a ordem de apresentação das questões.
     */
    private List<Integer> ordem;
    
    /**
     * Informa o tempo que o growl deve ser exibido.
     */
    private int delayGrowl = 5000;

    /**
     * Construtor padrão.
     */
    public QuestionarioEmRespostaMB() {
        inicializar();
    }
    
    /**
     * Inicializar o questionário em resposta buscando as informações do entrevistado e do questionário selecionado em outros controladores e
     * criando os mapas e a listagem de ordem de apresentação das questões.
     */
    public void inicializar(){
        FacesContext context = FacesContext.getCurrentInstance();
        entrevistado = ((AcessoMB) context.getELContext().getELResolver().getValue(context.getELContext(), null, "acessoMB")).getEntrevistado();
        
        questionario = ((QuestionariosDisponiveisMB) context.getELContext().getELResolver().getValue(context.getELContext(), null, "questionariosDisponiveisMB")).getQuestionarioSelecionado();

        List<Questao> qs = new ArrayList<>(questionario.getQuestoes());
        Collections.sort(qs);
        ordem = new ArrayList<>();
        questoes = new HashMap<>();
        respostas = new HashMap<>();
        for (int i = 0; i < qs.size(); i++) {
            ordem.add(i);
            questoes.put(i, qs.get(i));
            Resposta r;
            if (qs.get(i) instanceof QuestaoAberta) {
                r = new RespostaAberta();
                r.setQuestao(qs.get(i));
                r.setEntrevistado(entrevistado);                
            } else {

                QuestaoMultiplaEscolha qme = (QuestaoMultiplaEscolha) qs.get(i);                                
                List<Opcao> ops = new ArrayList<>(qme.getOpcoes());
                Collections.sort(ops);
                qme.setOpcoes(new TreeSet<>(ops));
                r = new RespostaMultiplaEscolha();
                r.setQuestao(qs.get(i));
                r.setEntrevistado(entrevistado);
                for (Opcao o : ops) {
                    RespostaMultiplaEscolha rme = (RespostaMultiplaEscolha) r;
                    OpcaoResposta or = new OpcaoResposta(false);
                    rme.getRespostas().put(o, or);
                }
            }
            respostas.put(i, r);
        }

        chaveDigitada = "";
        chaveAcessoLiberada = this.questionario.getChaveAcesso().equals("");
    }
    

    /**
     * 
     * Verifica se o usuário digitou a chave de acesso correta para o questionário.
     * 
     * @return caminho para o qual o sistema deve ser redirecionado.
     */
    public String validarChaveAcesso() {

        this.delayGrowl = 5000;

        if (this.chaveDigitada.equals(this.questionario.getChaveAcesso())) {
            chaveAcessoLiberada = true;
            Mensagem.mostrar("sucesso", "Olá!", "Chave de acesso liberada!");
        } else {
            chaveAcessoLiberada = false;
            Mensagem.mostrar("alerta", "Atenção", "Chave de acesso inválida.");
        }
        return "/entrevista/questionario";
    }

    /**
     * Salvar as respostas fornecidas ao questionário.
     * 
     * @return caminho para o qual o sistema deve ser redirecionado.
     */
    public String salvar() {

        //validações
        //feita por questão
        //obrigatórias? Foi respondida?
        //Se aberta -- respeita a quantidade de caracteres estipulada?
        //Se multipla escolha respeita a quantidade de opções que podem ser selecionadas?        
        StringBuilder sb = new StringBuilder();

        int quantidadeQuestoesErro = 0;

        for (Integer i : ordem) {
            String s = "<p> Questão " + (i + 1) + " </p>";
            boolean possuiErros = false;

            Resposta r = respostas.get(i);
            if (r instanceof RespostaAberta) {
                RespostaAberta ra = (RespostaAberta) r;
                QuestaoAberta qa = (QuestaoAberta) r.getQuestao();
                if (qa.isObrigatoria() && ra.getTexto().equals("")) {//obrigatória não respondida
                    possuiErros = true;
                    s += "<p> * É obrigatória. </p>";
                }

                if (ra.getTexto().length() > qa.getTamanho()) {//tamanho da resposta
                    possuiErros = true;
                    s += "<p> * A resposta deve ter até " + qa.getTamanho() + " caracteres. </p>";
                }

            } else {
                RespostaMultiplaEscolha rme = (RespostaMultiplaEscolha) r;
                QuestaoMultiplaEscolha qme = (QuestaoMultiplaEscolha) r.getQuestao();

                int contRespostas = 0;
                for (Opcao o : rme.getRespostas().keySet()) {
                    if (rme.getRespostas().get(o).isChecked()) {
                        contRespostas++;
                    }
                }

                if (qme.isObrigatoria() && contRespostas == 0) { //obrigatória não respondida
                    possuiErros = true;
                    s += "<p> * É obrigatória. </p>";
                }

                if (contRespostas < qme.getQuantidadeMinima() || contRespostas > qme.getQuantidadeMaxima()) {//verificar a quantidade de respostas.
                    possuiErros = true;
                    s += "<p> * Quantidade inválida de respostas. </p>";
                }
            }

            if (possuiErros) {
                quantidadeQuestoesErro++;
                sb.append(s);
            }
        }

        if (!sb.toString().equals("")) {
            delayGrowl = quantidadeQuestoesErro * 45 * 1000;
            Mensagem.mostrar("erro", "Atenção", sb.toString());
            return "/entrevista/questionario";
        } else {

            //gravar as respostas.
            Dao<RespostaAberta> respostaAbertaDao = new GenericDAO<>(RespostaAberta.class);
            Dao<RespostaMultiplaEscolha> respostaMultiplaEscolhaDao = new GenericDAO<>(RespostaMultiplaEscolha.class);
            Dao<Respondente> respondenteDao = new GenericDAO<>(Respondente.class);
            for (Integer i : ordem) {
                Resposta r = respostas.get(i);
                try {
                    if (r instanceof RespostaAberta) {
                        respostaAbertaDao.salvar((RespostaAberta) r);
                    } else {
                        RespostaMultiplaEscolha rme = (RespostaMultiplaEscolha) r;
                        for (Opcao o : rme.getRespostas().keySet()) {
                            if (rme.getRespostas().get(o).isChecked()) {
                                rme.getOpcoes().add(o);
                            }
                        }
                        respostaMultiplaEscolhaDao.salvar(rme);
                    }
                } catch (PersistenceException e) {
                }
            }

            //gravar na tabela respondentes.
            Respondente respondente = new Respondente(0, entrevistado, questionario,new Date());            
            try {
                respondenteDao.salvar(respondente);
            } catch (PersistenceException ex) {
            }
            
            
            delayGrowl = 5000;
            Mensagem.mostrar("sucesso", "Atenção", "Todas as restrições foram atendidas.");
            return "/entrevista/questionarios";
        }

    }

    /**
     * Informa se a questão atual é do tipo aberta.
     * @param q questão a ser avaliada.
     * @return true se for uma questão aberta.
     */
    public boolean isAberta(Questao q) {
        return (q instanceof QuestaoAberta);
    }

    /**
     * Cancela a resposta ao questionário. Nenhum dado é armazenado.
     * 
     * @return caminho para o qual o sistema deve ser redirecionado.
     */
    public String cancelar() {

        return "/entrevista/questionarios";
    }

    /**
     * 
     * @return entrevistado atual.
     */
    public Entrevistado getEntrevistado() {
        return entrevistado;
    }

    /**
     * 
     * @param entrevistado entrevistado atual.
     */
    public void setEntrevistado(Entrevistado entrevistado) {
        this.entrevistado = entrevistado;
    }

    /**
     * 
     * @return questionário em resposta.
     */
    public Questionario getQuestionario() {
        return questionario;
    }

    /**
     * 
     * @param questionario questionário em resposta.
     */
    public void setQuestionario(Questionario questionario) {
        this.questionario = questionario;
    }

    /**
     * 
     * @return true se a chave de acesso foi informada corretamente ou se nem era requerida.
     */
    public boolean isChaveAcessoLiberada() {
        return chaveAcessoLiberada;
    }

    /**
     * 
     * @param chaveAcessoLiberada true se a chave de acesso foi informada corretamente ou se nem era requerida.
     */
    public void setChaveAcessoLiberada(boolean chaveAcessoLiberada) {
        this.chaveAcessoLiberada = chaveAcessoLiberada;
    }

    /**
     * 
     * @return chave de acesso digitada na interface.
     */
    public String getChaveDigitada() {
        return chaveDigitada;
    }

    /**
     * 
     * @param chaveDigitada chave de acesso digitada na interface.
     */
    public void setChaveDigitada(String chaveDigitada) {
        this.chaveDigitada = chaveDigitada;
    }

    /**
     * 
     * @return mapa de questões.
     */
    public Map<Integer, Questao> getQuestoes() {
        return questoes;
    }

    /**
     * 
     * @param questoes mapa de questões.
     */
    public void setQuestoes(Map<Integer, Questao> questoes) {
        this.questoes = questoes;
    }

    /**
     * 
     * @return mapa de respostas.
     */
    public Map<Integer, Resposta> getRespostas() {
        return respostas;
    }

    /**
     * 
     * @param respostas mapa de respostas.
     */
    public void setRespostas(Map<Integer, Resposta> respostas) {
        this.respostas = respostas;
    }

    /**
     * 
     * @return listagem de ordem de apresentação das questões.
     */
    public List<Integer> getOrdem() {
        return ordem;
    }

    /**
     * 
     * @param ordem listagem de ordem de apresentação das questões.
     */
    public void setOrdem(List<Integer> ordem) {
        this.ordem = ordem;
    }

    /**
     * 
     * @return tempo de exibição do componente growl na interface.
     */
    public int getDelayGrowl() {
        return delayGrowl;
    }

    /**
     * 
     * @param delayGrowl tempo de exibição do componente growl na interface.
     */
    public void setDelayGrowl(int delayGrowl) {
        this.delayGrowl = delayGrowl;
    }

}