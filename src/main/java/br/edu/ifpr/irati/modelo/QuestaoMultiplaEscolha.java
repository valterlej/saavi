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
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

/**
 * Questão de múltipla escolha.
 *
 * @author Valter Estevam
 */
@Entity(name = "questaomultiplaescolha")
@PrimaryKeyJoinColumn(name = "idQuestao")
public class QuestaoMultiplaEscolha extends Questao implements Serializable {

    /**
     * Quantidade mínima de opções que devem ser selecionadas.
     */
    @Column(name = "quantidademinima")
    private int quantidadeMinima;

    /**
     * Quantidade máxima de opções que podem ser selecionadas.
     */
    @Column(name = "quantidademaxima")
    private int quantidadeMaxima;

    /**
     * Listagem de opções de resposta à questão.
     */
    @OneToMany(fetch = FetchType.LAZY)
    @Fetch(FetchMode.JOIN)
    private Set<Opcao> opcoes;

    /**
     * Construtor padrão.
     *
     * Quantidades mínima e máxima iguais a 1.
     */
    public QuestaoMultiplaEscolha() {
        super();
        quantidadeMinima = 1;
        quantidadeMaxima = 1;
        opcoes = new HashSet<>();
    }

    /**
     * Construtor
     *
     * @param opcoes opções de resposta possíveis.
     */
    public QuestaoMultiplaEscolha(Set<Opcao> opcoes) {
        super();
        quantidadeMinima = 1;
        quantidadeMaxima = 1;
        this.opcoes = opcoes;
    }

    /**
     *
     * @param quantidadeMinima quantidade mínima de opções a serem selecionadas.
     * @param quantidadeMaxima quantidade máxima de opções que podem ser
     * selecionadas.
     * @param opcoes listagem de opções de resposta.
     */
    public QuestaoMultiplaEscolha(int quantidadeMinima, int quantidadeMaxima, Set<Opcao> opcoes) {
        super();
        this.quantidadeMinima = quantidadeMinima;
        this.quantidadeMaxima = quantidadeMaxima;
        this.opcoes = opcoes;
    }

    /**
     *
     * @param quantidadeMinima quantidade mínima de opções a serem selecionadas.
     * @param quantidadeMaxima quantidade máxima de opções que podem ser
     * selecionadas.
     * @param opcoes listagem de opções de resposta.
     * @param idQuestao identificador único.
     * @param texto texto da questão.
     * @param dica dica para interpretação da questão.
     * @param rotulo rótulo da questão.
     * @param obrigatoria true se a questão é obrigatória.
     * @param ordem ordem de apresentação no questionário.
     * @param questionario questionário ao qual está associada.
     */
    public QuestaoMultiplaEscolha(int quantidadeMinima, int quantidadeMaxima, Set<Opcao> opcoes, int idQuestao, String texto, String dica, String rotulo, boolean obrigatoria, int ordem, Questionario questionario) {
        super(idQuestao, texto, dica, rotulo, obrigatoria, ordem, questionario);
        this.quantidadeMinima = quantidadeMinima;
        this.quantidadeMaxima = quantidadeMaxima;
        this.opcoes = opcoes;
    }

    /**
     *
     * @return opções de resposta possíveis.
     */
    public Set<Opcao> getOpcoes() {
        return opcoes;
    }

    /**
     *
     * @param opcoes opções de resposta possíveis.
     */
    public void setOpcoes(Set<Opcao> opcoes) {
        this.opcoes = opcoes;
    }

    /**
     *
     * @return quantidade mínima de opções a serem selecionadas.
     */
    public int getQuantidadeMinima() {
        return quantidadeMinima;
    }

    /**
     *
     * @param quantidadeMinima quantidade mínima de opções a serem selecionadas.
     */
    public void setQuantidadeMinima(int quantidadeMinima) {
        this.quantidadeMinima = quantidadeMinima;
    }

    /**
     *
     * @return quantidade máxima de opções que podem ser selecionadas.
     */
    public int getQuantidadeMaxima() {
        return quantidadeMaxima;
    }

    /**
     *
     * @param quantidadeMaxima quantidade máxima de opções que podem ser
     * selecionadas.
     */
    public void setQuantidadeMaxima(int quantidadeMaxima) {
        this.quantidadeMaxima = quantidadeMaxima;
    }

}