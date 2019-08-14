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
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.Type;

/**
 * Armazena a informação de quem já participou do processo de consulta.
 * 
 * @author Valter Estevam
 */
@Entity (name="respondente")
public class Respondente implements Serializable{
    
    /**
     * Identificador único.
     */
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int idRespondente;
    
    /**
     * Entrevistado que respondeu ao questionário.
     */
    @OneToOne
    private Entrevistado entrevistado;
    
    /**
     * Questionário respondido.
     */
    @OneToOne
    private Questionario questionario;

    /**
     * Data e hora da resposta ao questionário.
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataHora;

    /**
     * Verdadeiro se a resposta foi validada pela CPA.
     */
    @Type(type = "yes_no")
    private boolean validado;
    
    /**
     * Construtor padrão.
     * 
     * As respostas são criadas validadas por padrão.
     */
    public Respondente() {
        idRespondente = 0;
        entrevistado = new Entrevistado();
        questionario = new Questionario();
        validado = true;
    }

    /**
     * Construtor. 
     * 
     * @param idRespondente identificador único.
     * @param entrevistado entrevistado que respondeu ao questionário.
     * @param questionario questionário respondido.
     * @param dataHora data e hora da resposta ao questionário.
     */
    public Respondente(int idRespondente, Entrevistado entrevistado, Questionario questionario, Date dataHora) {
        this.idRespondente = idRespondente;
        this.entrevistado = entrevistado;
        this.questionario = questionario;
        this.dataHora = dataHora;
        this.validado = true;
    }        

    /**
     * Construtor. 
     * 
     * @param idRespondente identificador único.
     * @param entrevistado entrevistado que respondeu ao questionário.
     * @param questionario questionário respondido.
     * @param dataHora data e hora da resposta ao questionário.
     * @param validado true se a resposta foi validada pela CPA.
     */
    public Respondente(int idRespondente, Entrevistado entrevistado, Questionario questionario, Date dataHora, boolean validado) {
        this.idRespondente = idRespondente;
        this.entrevistado = entrevistado;
        this.questionario = questionario;
        this.dataHora = dataHora;
        this.validado = validado;
    }
            
    /**
     * 
     * @return identificador único.
     */
    public int getIdRespondente() {
        return idRespondente;
    }

    /**
     * 
     * @param idRespondente identificador único.
     */
    public void setIdRespondente(int idRespondente) {
        this.idRespondente = idRespondente;
    }

    /**
     * 
     * @return entrevistado que respondeu ao questionário.
     */
    public Entrevistado getEntrevistado() {
        return entrevistado;
    }

    /**
     * 
     * @param entrevistado entrevistado que respondeu ao questionário.
     */
    public void setEntrevistado(Entrevistado entrevistado) {
        this.entrevistado = entrevistado;
    }

    /**
     * 
     * @return questionário respondido.
     */
    public Questionario getQuestionario() {
        return questionario;
    }

    /**
     * 
     * @param questionario questionário respondido.
     */
    public void setQuestionario(Questionario questionario) {
        this.questionario = questionario;
    }

    /**
     * 
     * @return data e hora da resposta ao questionário.
     */
    public Date getDataHora() {
        return dataHora;
    }

    /**
     * 
     * @param dataHora data e hora da resposta ao questionário.
     */
    public void setDataHora(Date dataHora) {
        this.dataHora = dataHora;
    }

    /**
     * 
     * @return true se a resposta foi validada pela CPA.
     */
    public boolean isValidado() {
        return validado;
    }

    /**
     * 
     * @param validado true se a resposta foi validada pela CPA.
     */
    public void setValidado(boolean validado) {
        this.validado = validado;
    }
    
}