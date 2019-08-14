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
package br.edu.ifpr.irati.modelo;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

/**
 * Questionário
 *
 * @author Valter Estevam
 */
@Entity (name="questionario")
public class Questionario implements Serializable{
    
    /**
     * Identificador único.
     */
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int idQuestionario;
    
    /**
     * Nome completo do questionário.
     */
    @NotNull(message="Nome completo é obrigatório.")
    @Size(min=1, max=100, message = "O nome completo deve possuir entre 1 e 100 caracteres.")
    @Column(name = "nomecompleto", nullable = false, length = 100)
    private String nomeCompleto;
    
    /**
     * Nome curto utilizado em relatórios.
     */
    @Size(min=0, max=50, message = "O nome curto deve possuir entre 0 e 50 caracteres.")
    @Column(name = "nomecurto", nullable = true, length = 50)
    private String nomeCurto;
    
    /**
     * Data e hora de início da aplicação.
     */
    @NotNull(message = "Data e hora de início é obrigatória.")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataHoraInicio;
    
    /**
     * Data e hora de fim da aplicação.
     */
    @NotNull(message = "Data e hora de final é obrigatória.")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataHoraFinal;
    
    /**
     * Texto descritivo do questionário.
     */
    @Size(min=0, max=200, message = "O descrição deve possuir entre 0 e 200 caracteres.")
    @Column(name = "descricao", nullable = true, length = 200)
    private String descricao;

    /**
     * Conjunto de questões que compõem o questionário.
     */
    @OneToMany(fetch = FetchType.LAZY)
    @Fetch(FetchMode.JOIN)
    private Set<Questao> questoes;
    
    /**
     * Conjunto de categorias que podem responder ao questionário.
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @Fetch(FetchMode.JOIN)
    private Set<Categoria> categorias;
    
    /**
     * Código que deve ser inserido pelo entrevistado para liberação do questionário
     * 
     * Caso o valor esteja em branco não será solicitado.
     */
    @Size(min=0, max=20, message = "A chave de acesso deve possuir entre 0 e 20 caracteres.")
    @Column(name = "chaveacesso", nullable = true, length = 20)
    private String chaveAcesso;
    
    /**
     * Construtor padrão.
     */
    public Questionario() {
        idQuestionario = 0;
        nomeCompleto = "";
        nomeCurto = "";
        dataHoraInicio = new Date();
        dataHoraFinal = new Date();
        questoes = new TreeSet<>();
        categorias = new HashSet<>();
        chaveAcesso = "";
    }

    /**
     * 
     * @param idQuestionario identificador único.
     * @param nomeCompleto nome completo do questionário.
     * @param nomeCurto nome curto utilizado em relatórios.
     * @param dataHoraInicio data e hora de início da aplicação.
     * @param dataHoraFinal data e hora de fim da aplicação.
     * @param descricao descrição do questionário.
     * @param chaveAcesso chave de acesso para liberação de resposta.
     */
    public Questionario(int idQuestionario, String nomeCompleto, String nomeCurto, Date dataHoraInicio, Date dataHoraFinal, String descricao, String chaveAcesso) {
        this.idQuestionario = idQuestionario;
        this.nomeCompleto = nomeCompleto;
        this.nomeCurto = nomeCurto;
        this.dataHoraInicio = dataHoraInicio;
        this.dataHoraFinal = dataHoraFinal;
        this.descricao = descricao;
        this.chaveAcesso = chaveAcesso;
        questoes = new TreeSet<>();
        categorias = new HashSet<>();
    }

    /**
     * 
     * @param idQuestionario identificador único.
     * @param nomeCompleto nome completo do questionário.
     * @param nomeCurto nome curto utilizado em relatórios.
     * @param dataHoraInicio data e hora de início da aplicação.
     * @param dataHoraFinal data e hora de fim da aplicação.
     * @param descricao descrição do questionário.
     * @param chaveAcesso chave de acesso para liberação de resposta.
     * @param categorias conjunto de categorias que podem responder ao questionário.
     */
    public Questionario(int idQuestionario, String nomeCompleto, String nomeCurto, Date dataHoraInicio, Date dataHoraFinal, String descricao,String chaveAcesso, Set<Categoria> categorias) {
        this.idQuestionario = idQuestionario;
        this.nomeCompleto = nomeCompleto;
        this.nomeCurto = nomeCurto;
        this.dataHoraInicio = dataHoraInicio;
        this.dataHoraFinal = dataHoraFinal;
        this.descricao = descricao;
        this.chaveAcesso = chaveAcesso;
        this.questoes = new TreeSet<>();
        this.categorias = categorias;
    }
    
    /**
     * 
     * @param idQuestionario identificador único.
     * @param nomeCompleto nome completo do questionário.
     * @param nomeCurto nome curto utilizado em relatórios.
     * @param dataHoraInicio data e hora de início da aplicação.
     * @param dataHoraFinal data e hora de fim da aplicação.
     * @param descricao descrição do questionário.
     * @param chaveAcesso chave de acesso para liberação de resposta.
     * @param questoes conjunto de questões que compõem o questionário.
     * @param categorias conjunto de categorias que podem responder ao questionário.
     */
    public Questionario(int idQuestionario, String nomeCompleto, String nomeCurto, Date dataHoraInicio, Date dataHoraFinal, String descricao, String chaveAcesso, TreeSet<Questao> questoes, Set<Categoria> categorias) {
        this.idQuestionario = idQuestionario;
        this.nomeCompleto = nomeCompleto;
        this.nomeCurto = nomeCurto;
        this.dataHoraInicio = dataHoraInicio;
        this.dataHoraFinal = dataHoraFinal;
        this.descricao = descricao;
        this.chaveAcesso = chaveAcesso;
        this.questoes = questoes;
        this.categorias = categorias;
    }
                   
    /**
     * 
     * @return identificador único.
     */
    public int getIdQuestionario() {
        return idQuestionario;
    }

    /**
     * 
     * @param idQuestionario identificador único.
     */
    public void setIdQuestionario(int idQuestionario) {
        this.idQuestionario = idQuestionario;
    }

    /**
     * 
     * @return nome completo.
     */
    public String getNomeCompleto() {
        return nomeCompleto;
    }

    /**
     * 
     * @param nomeCompleto nome completo.
     */
    public void setNomeCompleto(String nomeCompleto) {
        this.nomeCompleto = nomeCompleto;
    }

    /**
     * 
     * @return nome curto utilizado em relatórios.
     */
    public String getNomeCurto() {
        return nomeCurto;
    }

    /**
     * 
     * @param nomeCurto nome curto utilizado em relatórios.
     */
    public void setNomeCurto(String nomeCurto) {
        this.nomeCurto = nomeCurto;
    }

    /**
     * 
     * @return data e hora de início da aplicação.
     */
    public Date getDataHoraInicio() {
        return dataHoraInicio;
    }

    /**
     * 
     * @param dataHoraInicio data e hora de início da aplicação.
     */
    public void setDataHoraInicio(Date dataHoraInicio) {
        this.dataHoraInicio = dataHoraInicio;
    }

    /**
     * 
     * @return data e hora de fim da aplicação.
     */
    public Date getDataHoraFinal() {
        return dataHoraFinal;
    }

    /**
     * 
     * @param dataHoraFinal data e hora de fim da aplicação.
     */
    public void setDataHoraFinal(Date dataHoraFinal) {
        this.dataHoraFinal = dataHoraFinal;
    }

    /**
     * 
     * @return descrição do questionário.
     */
    public String getDescricao() {
        return descricao;
    }

    /**
     * 
     * @param descricao descrição do questionário.
     */
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    /**
     * 
     * @return questões que compõem o questionário.
     */
    public Set<Questao> getQuestoes() {
        return questoes;
    }

    /**
     * 
     * @param questoes questões que compõem o questionário.
     */
    public void setQuestoes(TreeSet<Questao> questoes) {
        this.questoes = questoes;
    }

    /**
     * 
     * @return categorias que podem responder ao questionário.
     */
    public Set<Categoria> getCategorias() {
        return categorias;
    }

    /**
     * 
     * @param categorias categorias que podem responder ao questionário.
     */
    public void setCategorias(Set<Categoria> categorias) {
        this.categorias = categorias;
    }

    /**
     * 
     * @return chave de acesso para liberação do questionário.
     */
    public String getChaveAcesso() {
        return chaveAcesso;
    }

    /**
     * 
     * @param chaveAcesso chave de acesso para liberação do questionário.
     */
    public void setChaveAcesso(String chaveAcesso) {
        this.chaveAcesso = chaveAcesso;
    }
    
}