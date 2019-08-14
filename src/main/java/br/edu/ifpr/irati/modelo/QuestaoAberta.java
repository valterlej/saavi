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
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 *
 * Questão aberta.
 *
 * @author Valter Estevam
 */
@Entity(name = "questaoaberta")
@PrimaryKeyJoinColumn(name = "idQuestao")
public class QuestaoAberta extends Questao implements Serializable {

    /**
     * Quantidade de caracteres máxima para resposta. Deve estar entre 50 e 500
     * caracteres.
     */
    @Min(value = 50, message = "Tamanho deve ser maior do que 50 caracteres.")
    @Max(value = 500, message = "Tamanho deve ser menor do que 500 caracteres.")
    @Column(name = "tamanho")
    private int tamanho;

    /**
     * Construtor padrão.
     */
    public QuestaoAberta() {
        super();
        tamanho = 100;
    }

    /**
     * Construtor
     *
     * @param tamanho quantidade de caracteres.
     */
    public QuestaoAberta(int tamanho) {
        super();
        this.tamanho = tamanho;
    }

    /**
     * Construtor
     *
     * @param tamanho quantidade de caracteres para resposta.
     * @param idQuestao identificador da questão.
     * @param texto texto da questão.
     * @param dica dica da questão.
     * @param rotulo rótulo da questão.
     * @param obrigatoria true se a questão é obrigatória.
     * @param ordem ordem de apresentação no questionário.
     * @param questionario questionário associado.
     */
    public QuestaoAberta(int tamanho, int idQuestao, String texto, String dica, String rotulo, boolean obrigatoria, int ordem, Questionario questionario) {
        super(idQuestao, texto, dica, rotulo, obrigatoria, ordem, questionario);
        this.tamanho = tamanho;
    }

    /**
     *
     * @return tamanho da resposta.
     */
    public int getTamanho() {
        return tamanho;
    }

    /**
     *
     * @param tamanho tamanho da resposta.
     */
    public void setTamanho(int tamanho) {
        this.tamanho = tamanho;
    }

}