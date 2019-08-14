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
package br.edu.ifpr.irati.dao;

import br.edu.ifpr.irati.modelo.Entrevistado;
import br.edu.ifpr.irati.modelo.Questionario;
import br.edu.ifpr.irati.modelo.Respondente;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * Classe para acesso aos dados de um questionário.
 * 
 * @author Valter Estevam
 */
public class QuestionarioDAO implements Serializable{

    /**
     * Carrega todos os questionários que atendem às seguintes regras:
     *
     * - o questionário tem que estar disponível para a categoria do
     * entrevistado - a data atual deve estar entre a data de início e de fim do
     * cadastro - o questionário não pode ter sido respondido pelo entrevistado
     *
     * @param entrevistado
     * @return Lista de questionários aptos a serem respondidos
     */
    public List<Questionario> buscarQuestionariosAtivos(Entrevistado entrevistado) {

        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        String hqlQuestionarios = "from questionario as q inner join fetch q.categorias as cat where cat.idCategoria = :idcat and :dthoje between q.dataHoraInicio and q.dataHoraFinal";

        Query queryQuestionarios = session.createQuery(hqlQuestionarios).setParameter("idcat", entrevistado.getCategoria().getIdCategoria()).setParameter("dthoje", new Date());

        //lista de todos os questionários para a categoria do entrevistado com data válida
        List<Questionario> questionarios = (List<Questionario>) queryQuestionarios.list();

        //listar respostas do entrevistado
        String hqlRespondentes = "from respondente as r where r.entrevistado.idEntrevistado = :idEnt";
        Query queryRespondentes = session.createQuery(hqlRespondentes).setParameter("idEnt", entrevistado.getIdEntrevistado());

        List<Respondente> respostas = (List<Respondente>) queryRespondentes.list();

        //filtrar questionários a responder
        List<Questionario> filtrados = new ArrayList<>();
        for (Questionario q : questionarios) {
            boolean respondido = false;
            for (Respondente r : respostas) {
                if (q.getIdQuestionario() == r.getQuestionario().getIdQuestionario()) {
                    respondido = true;
                    break;
                }
            }
            if (!respondido) {
                filtrados.add(q);
            }
        }

        //encerrar sessão e retornar a lista de questionários
        session.getTransaction().commit();
        session.close();
        return filtrados;

    }
    
    /**
     * Retorna o histórico de respostas de um entrevistado
     * 
     * @param entrevistado
     * @return Lista de questionários respondidos.
     */
    public List<Respondente> buscarHistoricoRespostas(Entrevistado entrevistado){
        
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();     
        String hqlRespostas = "from respondente as r where r.entrevistado.idEntrevistado = :idEnt";
        Query queryRespostas = session.createQuery(hqlRespostas).setParameter("idEnt", entrevistado.getIdEntrevistado());
        List<Respondente> respostas = (List<Respondente>) queryRespostas.list();
        session.getTransaction().commit();
        session.close();
        return respostas;
        
    }

}