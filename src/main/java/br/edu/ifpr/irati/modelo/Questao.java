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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.annotations.Type;

/**
 * Questão
 *
 * @author Valter Estevam
 */
@Entity(name = "questao")
@Inheritance(strategy = InheritanceType.JOINED)
public class Questao implements Serializable, Comparable<Questao> {

    /**
     * Identificador único.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected int idQuestao;

    /**
     * Texto da questão.
     */
    @NotNull(message = "Texto é obrigatório")
    @Size(min=1, max=400, message = "O texto deve possuir entre 1 e 400 caracteres.")
    @Column(name = "texto", nullable = false, length = 400)
    protected String texto;

    /**
     * Dica para interpretação e resposta.
     */
    @Size(min=0, max=400, message = "A pode ter até 400 caracteres.")
    @Column(name = "dica", nullable = true, length = 400)
    protected String dica;

    /**
     * Rótulo associado com a questão.
     */
    @Size(min=0, max=80, message = "O rótulo pode ter até 80 caracteres.")
    @Column(name = "rotulo", nullable = true, length = 80)
    protected String rotulo;

    /**
     * Armazena a informação se a questão é obrigatória ou não.
     */
    @NotNull(message = "Você deve informar se a questão é obrigatória.")
    @Type(type = "yes_no")
    protected boolean obrigatoria;

    /**
     * Ordem de apresentação no questionário.
     */
    @Column(name = "ordem")
    protected int ordem;

    /**
     * Questionário associado.
     */
    @OneToOne
    protected Questionario questionario;

    /**
     * Construtor padrão.
     * 
     * Está publico para permitir uso pela Classe EntidadeFactory
     * 
     */
    public Questao() {
        idQuestao = 0;
        texto = "";
        dica = "";
        rotulo = "";
        obrigatoria = true;
        ordem = 0;
        questionario = new Questionario();
    }

    /**
     * Usado apenas a partir do comando super nas classes que implementam esta classe 
     * abstrata.
     * 
     * @param idQuestao identificador da questão.
     * @param texto texto da questão.
     * @param dica dica para interpretação ou resposta.
     * @param rotulo rótulo da questão.
     * @param obrigatoria true se for de resposta obrigatória.
     * @param ordem ordem de apresentação no questionário.
     * @param questionario questionário associado.
     */
    protected Questao(int idQuestao, String texto, String dica, String rotulo, boolean obrigatoria, int ordem, Questionario questionario) {
        this.idQuestao = idQuestao;
        this.texto = texto;
        this.dica = dica;
        this.rotulo = rotulo;
        this.obrigatoria = obrigatoria;
        this.ordem = ordem;
        this.questionario = questionario;
    }

    /**
     * 
     * @return identificador da questão.
     */
    public int getIdQuestao() {
        return idQuestao;
    }

    /**
     * 
     * @param idQuestao identificador da questão.
     */
    public void setIdQuestao(int idQuestao) {
        this.idQuestao = idQuestao;
    }

    /**
     * 
     * @return texto da questão.
     */
    public String getTexto() {
        return texto;
    }

    /**
     * 
     * @param texto texto da questão.
     */
    public void setTexto(String texto) {
        this.texto = texto;
    }

    /**
     * 
     * @return dica para interpretação ou resposta.
     */
    public String getDica() {
        return dica;
    }

    /**
     * 
     * @param dica dica para interpretação ou resposta.
     */
    public void setDica(String dica) {
        this.dica = dica;
    }

    /**
     * 
     * @return rótulo da questão.
     */
    public String getRotulo() {
        return rotulo;
    }

    /**
     * 
     * @param rotulo rótulo da questão.
     */
    public void setRotulo(String rotulo) {
        this.rotulo = rotulo;
    }

    /**
     * 
     * @return questionário associado.
     */
    public Questionario getQuestionario() {
        return questionario;
    }

    /**
     * 
     * @param questionario questionário associado.
     */
    public void setQuestionario(Questionario questionario) {
        this.questionario = questionario;
    }

    /**
     * 
     * @return retorna o String "*" caso seja obrigatória.
     */
    public String getObrigatoriaString(){
        if (obrigatoria){
            return "*";
        }else{
            return "";
        }
    }
    
    /**
     * 
     * @return true se for de resposta obrigatória.
     */
    public boolean isObrigatoria() {
        return obrigatoria;
    }

    /**
     * 
     * @param obrigatoria true se for de resposta obrigatória.
     */
    public void setObrigatoria(boolean obrigatoria) {
        this.obrigatoria = obrigatoria;
    }

    /**
     * 
     * @return ordem de apresentação no questionário.
     */
    public int getOrdem() {
        return ordem;
    }

    /**
     * 
     * @param ordem ordem de apresentação no questionário.
     */
    public void setOrdem(int ordem) {
        this.ordem = ordem;
    }

    /**
     * Método utilizado para comparação entre entre objetos para fins de ordenação.
     * A comparação se dá pela ordem.
     * 
     * @param o objeto a ser comparado.
     * @return 0 se forem iguais, menor que 0 se o atual é vem antes do comparado e maior do que zero de o objeto comparado vem antes do atual.
     */
    @Override
    public int compareTo(Questao o) {
        if (this.ordem > o.getOrdem()) {
            return 1;
        } else if (this.ordem < o.getOrdem()) {
            return -1;
        } else {
            return 0;
        }
    }

    /**
     * Método usado para verificar a igualdade entre objetos.
     * A comparação é feita pelo id.
     * 
     * @param obj objeto a ser comparado.
     * @return true se forem iguais.
     */
    @Override
    public boolean equals(Object obj) {
        Questao q = (Questao) obj;
        return this.getIdQuestao() == q.getIdQuestao();
    }
           
}