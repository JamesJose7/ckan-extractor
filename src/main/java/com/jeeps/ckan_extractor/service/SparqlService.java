package com.jeeps.ckan_extractor.service;

import com.jeeps.ckan_extractor.model.ConfigurationRegistry;
import com.jeeps.ckan_extractor.model.ConfigurationSingleton;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.RDFNode;
import virtuoso.jena.driver.VirtModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SparqlService {

    public static List<List<String>> queryEndpoint(String endpoint, String query, String... vars) {
        HttpClient client =
                HttpClientBuilder.create()
                        .useSystemProperties()
                        .setRedirectStrategy(new LaxRedirectStrategy())
                        .setConnectionManager(new BasicHttpClientConnectionManager())
//                        .setMaxConnPerRoute(200000)
//                        .setMaxConnTotal(10000)
//                        .setConnectionTimeToLive(10, TimeUnit.SECONDS)
                        .build();
        QueryExecution queryExecution = QueryExecutionFactory.sparqlService(endpoint, query, client);
        return query(queryExecution, vars);
    }

    public static List<List<String>> queryModel(Model model, String query, String... vars) {
        QueryExecution queryExecution = QueryExecutionFactory.create(query, model);
        return query(queryExecution, vars);
    }

    private static List<List<String>> query(QueryExecution qE, String... vars) {
        List<List<String>> resultSet = new ArrayList<>();
        resultSet.add(new ArrayList<>(Arrays.asList(vars)));

        ResultSet results = qE.execSelect();

        while (results.hasNext()) {
            List<String> row = new ArrayList<>();
            QuerySolution sol = results.nextSolution();

            // get every var
            for (String var : vars) {
                RDFNode node = sol.get(var);
                if (var.equals("count"))
                    row.add(node.toString().split("\\^")[0]);
                else
                    row.add(node.toString());
            }
            resultSet.add(row);
        }
        return resultSet;
    }

    public static void uploadModelToTriplestore(Model model, String graph) {
        ConfigurationRegistry configurationRegistry = ConfigurationSingleton.getInstance().getConfigurationRegistry();
        VirtModel virtualModel = VirtModel.openDatabaseModel(
                graph,
                configurationRegistry.getSparqlDBEndpoint(),
                configurationRegistry.getSparqlDBUser(),
                configurationRegistry.getSparqlDBPass());
        //Add model
        virtualModel.add(model);
    }
}
