package com.jeeps.ckan_extractor.service;

import com.jeeps.ckan_extractor.model.SdgConceptScheme;
import com.jeeps.ckan_extractor.model.SdgConceptTree;
import com.jeeps.ckan_extractor.model.SdgRelatedDataset;
import org.apache.jena.rdf.model.Model;

import java.util.List;
import java.util.Map;

public interface KnowledgeBaseService {
    List<SdgRelatedDataset> findAllCatalogsRelatedToOds();
    Map<String, Integer> howManyDatasetsRelateToEachGoal();
    List<SdgRelatedDataset> getRelatedOdsByDatasetId(Long id);
    SdgConceptScheme getSdgConcepts(int sdg);
    SdgConceptTree getSdgConceptTree(int sdg);
    void uploadCatalogsModel(Model model);
    void uploadSdgModel(Model model);
    void uploadSdgOdLinks(Model model);
}
