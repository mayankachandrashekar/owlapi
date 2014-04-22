/* This file is part of the OWL API.
 * The contents of this file are subject to the LGPL License, Version 3.0.
 * Copyright 2014, The University of Manchester
 * 
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program.  If not, see http://www.gnu.org/licenses/.
 *
 * Alternatively, the contents of this file may be used under the terms of the Apache License, Version 2.0 in which case, the provisions of the Apache License Version 2.0 are applicable instead of those above.
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License. */
package org.semanticweb.owlapi.owlxml.parser;

import static org.semanticweb.owlapi.vocab.OWLXMLVocabulary.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.AddImport;
import org.semanticweb.owlapi.model.AddOntologyAnnotation;
import org.semanticweb.owlapi.model.HasIRI;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLAnnotationSubject;
import org.semanticweb.owlapi.model.OWLAnnotationValue;
import org.semanticweb.owlapi.model.OWLAnonymousIndividual;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataPropertyExpression;
import org.semanticweb.owlapi.model.OWLDataRange;
import org.semanticweb.owlapi.model.OWLDatatype;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLFacetRestriction;
import org.semanticweb.owlapi.model.OWLImportsDeclaration;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyID;
import org.semanticweb.owlapi.model.OWLPropertyExpression;
import org.semanticweb.owlapi.model.OWLRuntimeException;
import org.semanticweb.owlapi.model.SWRLAtom;
import org.semanticweb.owlapi.model.SWRLDArgument;
import org.semanticweb.owlapi.model.SWRLIArgument;
import org.semanticweb.owlapi.model.SWRLVariable;
import org.semanticweb.owlapi.model.SetOntologyID;
import org.semanticweb.owlapi.model.UnloadableImportException;
import org.semanticweb.owlapi.vocab.Namespaces;
import org.semanticweb.owlapi.vocab.OWLFacet;
import org.semanticweb.owlapi.vocab.OWLXMLVocabulary;

import com.google.common.base.Optional;

/**
 * @author Matthew Horridge, The University Of Manchester, Bio-Health
 *         Informatics Group
 * @since 2.0.0
 */
public enum PARSER_OWLXMLVocabulary implements HasIRI {
//@formatter:off
    /** CLASS                               */  PARSER_CLASS                               (CLASS                               ) { @Nonnull
                                                                                                                                    @Override public OWLElementHandler<?> createHandler(@Nonnull OWLXMLParserHandler handler) { return new OWLClassElementHandler(handler); } },
    /** DATA_PROPERTY                       */  PARSER_DATA_PROPERTY                       (DATA_PROPERTY                       ) { @Nonnull
                                                                                                                                    @Override public OWLElementHandler<?> createHandler(@Nonnull OWLXMLParserHandler handler) { return new OWLDataPropertyElementHandler(handler); } },
    /** OBJECT_PROPERTY                     */  PARSER_OBJECT_PROPERTY                     (OBJECT_PROPERTY                     ) { @Nonnull
                                                                                                                                    @Override public OWLElementHandler<?> createHandler(@Nonnull OWLXMLParserHandler handler) { return new OWLObjectPropertyElementHandler(handler); } },
    /** NAMED_INDIVIDUAL                    */  PARSER_NAMED_INDIVIDUAL                    (NAMED_INDIVIDUAL                    ) { @Nonnull
                                                                                                                                    @Override public OWLElementHandler<?> createHandler(@Nonnull OWLXMLParserHandler handler) { return new OWLIndividualElementHandler(handler); } },
    /** ENTITY_ANNOTATION                   */  PARSER_ENTITY_ANNOTATION                   (ENTITY_ANNOTATION                   ) { @Nonnull
                                                                                                                                    @Override public OWLElementHandler<?> createHandler(@Nonnull OWLXMLParserHandler handler) { return new LegacyEntityAnnotationElementHandler(handler); } },
    /** ANNOTATION_PROPERTY                 */  PARSER_ANNOTATION_PROPERTY                 (ANNOTATION_PROPERTY                 ) { @Nonnull
                                                                                                                                    @Override public OWLElementHandler<?> createHandler(@Nonnull OWLXMLParserHandler handler) { return new OWLAnnotationPropertyElementHandler(handler); } },
    /** DATATYPE                            */  PARSER_DATATYPE                            (DATATYPE                            ) { @Nonnull
                                                                                                                                    @Override public OWLElementHandler<?> createHandler(@Nonnull OWLXMLParserHandler handler) { return new OWLDatatypeElementHandler(handler); } },
    /** ANNOTATION                          */  PARSER_ANNOTATION                          (ANNOTATION                          ) { @Nonnull
                                                                                                                                    @Override public OWLElementHandler<?> createHandler(@Nonnull OWLXMLParserHandler handler) { return new OWLAnnotationElementHandler(handler); } },
    /** ANONYMOUS_INDIVIDUAL                */  PARSER_ANONYMOUS_INDIVIDUAL                (ANONYMOUS_INDIVIDUAL                ) { @Nonnull
                                                                                                                                    @Override public OWLElementHandler<?> createHandler(@Nonnull OWLXMLParserHandler handler) { return new OWLAnonymousIndividualElementHandler(handler); } },
    /** IMPORT                              */  PARSER_IMPORT                              (IMPORT                              ) { @Nonnull
                                                                                                                                    @Override public OWLElementHandler<?> createHandler(@Nonnull OWLXMLParserHandler handler) { return new OWLImportsHandler(handler); } },
    /** ONTOLOGY                            */  PARSER_ONTOLOGY                            (ONTOLOGY                            ) { @Nonnull
                                                                                                                                    @Override public OWLElementHandler<?> createHandler(@Nonnull OWLXMLParserHandler handler) { return new OWLOntologyHandler(handler); } },
    /** LITERAL                             */  PARSER_LITERAL                             (LITERAL                             ) { @Nonnull
                                                                                                                                    @Override public OWLElementHandler<?> createHandler(@Nonnull OWLXMLParserHandler handler) { return new OWLLiteralElementHandler(handler); } },
    /** OBJECT_INVERSE_OF                   */  PARSER_OBJECT_INVERSE_OF                   (OBJECT_INVERSE_OF                   ) { @Nonnull
                                                                                                                                    @Override public OWLElementHandler<?> createHandler(@Nonnull OWLXMLParserHandler handler) { return new OWLInverseObjectPropertyElementHandler(handler); } },
    /** DATA_COMPLEMENT_OF                  */  PARSER_DATA_COMPLEMENT_OF                  (DATA_COMPLEMENT_OF                  ) { @Nonnull
                                                                                                                                    @Override public OWLElementHandler<?> createHandler(@Nonnull OWLXMLParserHandler handler) { return new OWLDataComplementOfElementHandler(handler); } },
    /** DATA_ONE_OF                         */  PARSER_DATA_ONE_OF                         (DATA_ONE_OF                         ) { @Nonnull
                                                                                                                                    @Override public OWLElementHandler<?> createHandler(@Nonnull OWLXMLParserHandler handler) { return new OWLDataOneOfElementHandler(handler); } },
    /** DATATYPE_RESTRICTION                */  PARSER_DATATYPE_RESTRICTION                (DATATYPE_RESTRICTION                ) { @Nonnull
                                                                                                                                    @Override public OWLElementHandler<?> createHandler(@Nonnull OWLXMLParserHandler handler) { return new OWLDatatypeRestrictionElementHandler(handler); } },
    /** FACET_RESTRICTION                   */  PARSER_FACET_RESTRICTION                   (FACET_RESTRICTION                   ) { @Nonnull
                                                                                                                                    @Override public OWLElementHandler<?> createHandler(@Nonnull OWLXMLParserHandler handler) { return new OWLDatatypeFacetRestrictionElementHandler(handler); } },
    /** DATA_UNION_OF                       */  PARSER_DATA_UNION_OF                       (DATA_UNION_OF                       ) { @Nonnull
                                                                                                                                    @Override public OWLElementHandler<?> createHandler(@Nonnull OWLXMLParserHandler handler) { return new OWLDataUnionOfElementHandler(handler); } },
    /** DATA_INTERSECTION_OF                */  PARSER_DATA_INTERSECTION_OF                (DATA_INTERSECTION_OF                ) { @Nonnull
                                                                                                                                    @Override public OWLElementHandler<?> createHandler(@Nonnull OWLXMLParserHandler handler) { return new OWLDataIntersectionOfElementHandler(handler); } },
    /** OBJECT_INTERSECTION_OF              */  PARSER_OBJECT_INTERSECTION_OF              (OBJECT_INTERSECTION_OF              ) { @Nonnull
                                                                                                                                    @Override public OWLElementHandler<?> createHandler(@Nonnull OWLXMLParserHandler handler) { return new OWLObjectIntersectionOfElementHandler(handler); } },
    /** OBJECT_UNION_OF                     */  PARSER_OBJECT_UNION_OF                     (OBJECT_UNION_OF                     ) { @Nonnull
                                                                                                                                    @Override public OWLElementHandler<?> createHandler(@Nonnull OWLXMLParserHandler handler) { return new OWLObjectUnionOfElementHandler(handler); } },
    /** OBJECT_COMPLEMENT_OF                */  PARSER_OBJECT_COMPLEMENT_OF                (OBJECT_COMPLEMENT_OF                ) { @Nonnull
                                                                                                                                    @Override public OWLElementHandler<?> createHandler(@Nonnull OWLXMLParserHandler handler) { return new OWLObjectComplementOfElementHandler(handler); } },
    /** OBJECT_ONE_OF                       */  PARSER_OBJECT_ONE_OF                       (OBJECT_ONE_OF                       ) { @Nonnull
                                                                                                                                    @Override public OWLElementHandler<?> createHandler(@Nonnull OWLXMLParserHandler handler) { return new OWLObjectOneOfElementHandler(handler); } },
    /** OBJECT_SOME_VALUES_FROM             */  PARSER_OBJECT_SOME_VALUES_FROM             (OBJECT_SOME_VALUES_FROM             ) { @Nonnull
                                                                                                                                    @Override public OWLElementHandler<?> createHandler(@Nonnull OWLXMLParserHandler handler) { return new OWLObjectSomeValuesFromElementHandler(handler); } },
    /** OBJECT_ALL_VALUES_FROM              */  PARSER_OBJECT_ALL_VALUES_FROM              (OBJECT_ALL_VALUES_FROM              ) { @Nonnull
                                                                                                                                    @Override public OWLElementHandler<?> createHandler(@Nonnull OWLXMLParserHandler handler) { return new OWLObjectAllValuesFromElementHandler(handler); } },
    /** OBJECT_HAS_SELF                     */  PARSER_OBJECT_HAS_SELF                     (OBJECT_HAS_SELF                     ) { @Nonnull
                                                                                                                                    @Override public OWLElementHandler<?> createHandler(@Nonnull OWLXMLParserHandler handler) { return new OWLObjectExistsSelfElementHandler(handler); } },
    /** OBJECT_HAS_VALUE                    */  PARSER_OBJECT_HAS_VALUE                    (OBJECT_HAS_VALUE                    ) { @Nonnull
                                                                                                                                    @Override public OWLElementHandler<?> createHandler(@Nonnull OWLXMLParserHandler handler) { return new OWLObjectHasValueElementHandler(handler); } },
    /** OBJECT_MIN_CARDINALITY              */  PARSER_OBJECT_MIN_CARDINALITY              (OBJECT_MIN_CARDINALITY              ) { @Nonnull
                                                                                                                                    @Override public OWLElementHandler<?> createHandler(@Nonnull OWLXMLParserHandler handler) { return new OWLObjectMinCardinalityElementHandler(handler); } },
    /** OBJECT_EXACT_CARDINALITY            */  PARSER_OBJECT_EXACT_CARDINALITY            (OBJECT_EXACT_CARDINALITY            ) { @Nonnull
                                                                                                                                    @Override public OWLElementHandler<?> createHandler(@Nonnull OWLXMLParserHandler handler) { return new OWLObjectExactCardinalityElementHandler(handler); } },
    /** OBJECT_MAX_CARDINALITY              */  PARSER_OBJECT_MAX_CARDINALITY              (OBJECT_MAX_CARDINALITY              ) { @Nonnull
                                                                                                                                    @Override public OWLElementHandler<?> createHandler(@Nonnull OWLXMLParserHandler handler) { return new OWLObjectMaxCardinalityElementHandler(handler); } },
    /** DATA_SOME_VALUES_FROM               */  PARSER_DATA_SOME_VALUES_FROM               (DATA_SOME_VALUES_FROM               ) { @Nonnull
                                                                                                                                    @Override public OWLElementHandler<?> createHandler(@Nonnull OWLXMLParserHandler handler) { return new OWLDataSomeValuesFromElementHandler(handler); } },
    /** DATA_ALL_VALUES_FROM                */  PARSER_DATA_ALL_VALUES_FROM                (DATA_ALL_VALUES_FROM                ) { @Nonnull
                                                                                                                                    @Override public OWLElementHandler<?> createHandler(@Nonnull OWLXMLParserHandler handler) { return new OWLDataAllValuesFromElementHandler(handler); } },
    /** DATA_HAS_VALUE                      */  PARSER_DATA_HAS_VALUE                      (DATA_HAS_VALUE                      ) { @Nonnull
                                                                                                                                    @Override public OWLElementHandler<?> createHandler(@Nonnull OWLXMLParserHandler handler) { return new OWLDataHasValueElementHandler(handler); } },
    /** DATA_MIN_CARDINALITY                */  PARSER_DATA_MIN_CARDINALITY                (DATA_MIN_CARDINALITY                ) { @Nonnull
                                                                                                                                    @Override public OWLElementHandler<?> createHandler(@Nonnull OWLXMLParserHandler handler) { return new OWLDataMinCardinalityElementHandler(handler); } },
    /** DATA_EXACT_CARDINALITY              */  PARSER_DATA_EXACT_CARDINALITY              (DATA_EXACT_CARDINALITY              ) { @Nonnull
                                                                                                                                    @Override public OWLElementHandler<?> createHandler(@Nonnull OWLXMLParserHandler handler) { return new OWLDataExactCardinalityElementHandler(handler); } },
    /** DATA_MAX_CARDINALITY                */  PARSER_DATA_MAX_CARDINALITY                (DATA_MAX_CARDINALITY                ) { @Nonnull
                                                                                                                                    @Override public OWLElementHandler<?> createHandler(@Nonnull OWLXMLParserHandler handler) { return new OWLDataMaxCardinalityElementHandler(handler); } },
    /** SUB_CLASS_OF                        */  PARSER_SUB_CLASS_OF                        (SUB_CLASS_OF                        ) { @Nonnull
                                                                                                                                    @Override public OWLElementHandler<?> createHandler(@Nonnull OWLXMLParserHandler handler) { return new OWLSubClassAxiomElementHandler(handler); } },
    /** EQUIVALENT_CLASSES                  */  PARSER_EQUIVALENT_CLASSES                  (EQUIVALENT_CLASSES                  ) { @Nonnull
                                                                                                                                    @Override public OWLElementHandler<?> createHandler(@Nonnull OWLXMLParserHandler handler) { return new OWLEquivalentClassesAxiomElementHandler(handler); } },
    /** DISJOINT_CLASSES                    */  PARSER_DISJOINT_CLASSES                    (DISJOINT_CLASSES                    ) { @Nonnull
                                                                                                                                    @Override public OWLElementHandler<?> createHandler(@Nonnull OWLXMLParserHandler handler) { return new OWLDisjointClassesAxiomElementHandler(handler); } },
    /** DISJOINT_UNION                      */  PARSER_DISJOINT_UNION                      (DISJOINT_UNION                      ) { @Nonnull
                                                                                                                                    @Override public OWLElementHandler<?> createHandler(@Nonnull OWLXMLParserHandler handler) { return new OWLDisjointUnionElementHandler(handler); } },
    /** UNION_OF                            */  PARSER_UNION_OF                            (UNION_OF                            ) { @Nonnull
                                                                                                                                    @Override public OWLElementHandler<?> createHandler(@Nonnull OWLXMLParserHandler handler) { return new OWLUnionOfElementHandler(handler); } },
    /** SUB_OBJECT_PROPERTY_OF              */  PARSER_SUB_OBJECT_PROPERTY_OF              (SUB_OBJECT_PROPERTY_OF              ) { @Nonnull
                                                                                                                                    @Override public OWLElementHandler<?> createHandler(@Nonnull OWLXMLParserHandler handler) { return new OWLSubObjectPropertyOfAxiomElementHandler(handler); } },
    /** OBJECT_PROPERTY_CHAIN               */  PARSER_OBJECT_PROPERTY_CHAIN               (OBJECT_PROPERTY_CHAIN               ) { @Nonnull
                                                                                                                                    @Override public OWLElementHandler<?> createHandler(@Nonnull OWLXMLParserHandler handler) { return new OWLSubObjectPropertyChainElementHandler(handler); } },
    /** EQUIVALENT_OBJECT_PROPERTIES        */  PARSER_EQUIVALENT_OBJECT_PROPERTIES        (EQUIVALENT_OBJECT_PROPERTIES        ) { @Nonnull
                                                                                                                                    @Override public OWLElementHandler<?> createHandler(@Nonnull OWLXMLParserHandler handler) { return new OWLEquivalentObjectPropertiesAxiomElementHandler(             handler); } },
    /** DISJOINT_OBJECT_PROPERTIES          */  PARSER_DISJOINT_OBJECT_PROPERTIES          (DISJOINT_OBJECT_PROPERTIES          ) { @Nonnull
                                                                                                                                    @Override public OWLElementHandler<?> createHandler(@Nonnull OWLXMLParserHandler handler) { return new OWLDisjointObjectPropertiesAxiomElementHandler(             handler); } },
    /** OBJECT_PROPERTY_DOMAIN              */  PARSER_OBJECT_PROPERTY_DOMAIN              (OBJECT_PROPERTY_DOMAIN              ) { @Nonnull
                                                                                                                                    @Override public OWLElementHandler<?> createHandler(@Nonnull OWLXMLParserHandler handler) { return new OWLObjectPropertyDomainElementHandler(handler); } },
    /** OBJECT_PROPERTY_RANGE               */  PARSER_OBJECT_PROPERTY_RANGE               (OBJECT_PROPERTY_RANGE               ) { @Nonnull
                                                                                                                                    @Override public OWLElementHandler<?> createHandler(@Nonnull OWLXMLParserHandler handler) { return new OWLObjectPropertyRangeAxiomElementHandler(handler); } },
    /** INVERSE_OBJECT_PROPERTIES           */  PARSER_INVERSE_OBJECT_PROPERTIES           (INVERSE_OBJECT_PROPERTIES           ) { @Nonnull
                                                                                                                                    @Override public OWLElementHandler<?> createHandler(@Nonnull OWLXMLParserHandler handler) { return new OWLInverseObjectPropertiesAxiomElementHandler(             handler); } },
    /** FUNCTIONAL_OBJECT_PROPERTY          */  PARSER_FUNCTIONAL_OBJECT_PROPERTY          (FUNCTIONAL_OBJECT_PROPERTY          ) { @Nonnull
                                                                                                                                    @Override public OWLElementHandler<?> createHandler(@Nonnull OWLXMLParserHandler handler) { return new OWLFunctionalObjectPropertyAxiomElementHandler(             handler); } },
    /** INVERSE_FUNCTIONAL_OBJECT_PROPERTY  */  PARSER_INVERSE_FUNCTIONAL_OBJECT_PROPERTY  (INVERSE_FUNCTIONAL_OBJECT_PROPERTY  ) { @Nonnull
                                                                                                                                    @Override public OWLElementHandler<?> createHandler(@Nonnull OWLXMLParserHandler handler) { return new OWLInverseFunctionalObjectPropertyAxiomElementHandler(             handler); } },
    /** SYMMETRIC_OBJECT_PROPERTY           */  PARSER_SYMMETRIC_OBJECT_PROPERTY           (SYMMETRIC_OBJECT_PROPERTY           ) { @Nonnull
                                                                                                                                    @Override public OWLElementHandler<?> createHandler(@Nonnull OWLXMLParserHandler handler) { return new OWLSymmetricObjectPropertyAxiomElementHandler(             handler); } },
    /** ASYMMETRIC_OBJECT_PROPERTY          */  PARSER_ASYMMETRIC_OBJECT_PROPERTY          (ASYMMETRIC_OBJECT_PROPERTY          ) { @Nonnull
                                                                                                                                    @Override public OWLElementHandler<?> createHandler(@Nonnull OWLXMLParserHandler handler) { return new OWLAsymmetricObjectPropertyElementHandler(handler); } },
    /** REFLEXIVE_OBJECT_PROPERTY           */  PARSER_REFLEXIVE_OBJECT_PROPERTY           (REFLEXIVE_OBJECT_PROPERTY           ) { @Nonnull
                                                                                                                                    @Override public OWLElementHandler<?> createHandler(@Nonnull OWLXMLParserHandler handler) { return new OWLReflexiveObjectPropertyAxiomElementHandler(             handler); } },
    /** IRREFLEXIVE_OBJECT_PROPERTY         */  PARSER_IRREFLEXIVE_OBJECT_PROPERTY         (IRREFLEXIVE_OBJECT_PROPERTY         ) { @Nonnull
                                                                                                                                    @Override public OWLElementHandler<?> createHandler(@Nonnull OWLXMLParserHandler handler) { return new OWLIrreflexiveObjectPropertyAxiomElementHandler(             handler); } },
    /** TRANSITIVE_OBJECT_PROPERTY          */  PARSER_TRANSITIVE_OBJECT_PROPERTY          (TRANSITIVE_OBJECT_PROPERTY          ) { @Nonnull
                                                                                                                                    @Override public OWLElementHandler<?> createHandler(@Nonnull OWLXMLParserHandler handler) { return new OWLTransitiveObjectPropertyAxiomElementHandler(             handler); } },
    /** SUB_DATA_PROPERTY_OF                */  PARSER_SUB_DATA_PROPERTY_OF                (SUB_DATA_PROPERTY_OF                ) { @Nonnull
                                                                                                                                    @Override public OWLElementHandler<?> createHandler(@Nonnull OWLXMLParserHandler handler) { return new OWLSubDataPropertyOfAxiomElementHandler(handler); } },
    /** EQUIVALENT_DATA_PROPERTIES          */  PARSER_EQUIVALENT_DATA_PROPERTIES          (EQUIVALENT_DATA_PROPERTIES          ) { @Nonnull
                                                                                                                                    @Override public OWLElementHandler<?> createHandler(@Nonnull OWLXMLParserHandler handler) { return new OWLEquivalentDataPropertiesAxiomElementHandler(             handler); } },
    /** DISJOINT_DATA_PROPERTIES            */  PARSER_DISJOINT_DATA_PROPERTIES            (DISJOINT_DATA_PROPERTIES            ) { @Nonnull
                                                                                                                                    @Override public OWLElementHandler<?> createHandler(@Nonnull OWLXMLParserHandler handler) { return new OWLDisjointDataPropertiesAxiomElementHandler(handler); } },
    /** DATA_PROPERTY_DOMAIN                */  PARSER_DATA_PROPERTY_DOMAIN                (DATA_PROPERTY_DOMAIN                ) { @Nonnull
                                                                                                                                    @Override public OWLElementHandler<?> createHandler(@Nonnull OWLXMLParserHandler handler) { return new OWLDataPropertyDomainAxiomElementHandler(handler); } },
    /** DATA_PROPERTY_RANGE                 */  PARSER_DATA_PROPERTY_RANGE                 (DATA_PROPERTY_RANGE                 ) { @Nonnull
                                                                                                                                    @Override public OWLElementHandler<?> createHandler(@Nonnull OWLXMLParserHandler handler) { return new OWLDataPropertyRangeAxiomElementHandler(handler); } },
    /** FUNCTIONAL_DATA_PROPERTY            */  PARSER_FUNCTIONAL_DATA_PROPERTY            (FUNCTIONAL_DATA_PROPERTY            ) { @Nonnull
                                                                                                                                    @Override public OWLElementHandler<?> createHandler(@Nonnull OWLXMLParserHandler handler) { return new OWLFunctionalDataPropertyAxiomElementHandler(handler); } },
    /** SAME_INDIVIDUAL                     */  PARSER_SAME_INDIVIDUAL                     (SAME_INDIVIDUAL                     ) { @Nonnull
                                                                                                                                    @Override public OWLElementHandler<?> createHandler(@Nonnull OWLXMLParserHandler handler) { return new OWLSameIndividualsAxiomElementHandler(handler); } },
    /** DIFFERENT_INDIVIDUALS               */  PARSER_DIFFERENT_INDIVIDUALS               (DIFFERENT_INDIVIDUALS               ) { @Nonnull
                                                                                                                                    @Override public OWLElementHandler<?> createHandler(@Nonnull OWLXMLParserHandler handler) { return new OWLDifferentIndividualsAxiomElementHandler(handler); } },
    /** CLASS_ASSERTION                     */  PARSER_CLASS_ASSERTION                     (CLASS_ASSERTION                     ) { @Nonnull
                                                                                                                                    @Override public OWLElementHandler<?> createHandler(@Nonnull OWLXMLParserHandler handler) { return new OWLClassAssertionAxiomElementHandler(handler); } },
    /** OBJECT_PROPERTY_ASSERTION           */  PARSER_OBJECT_PROPERTY_ASSERTION           (OBJECT_PROPERTY_ASSERTION           ) { @Nonnull
                                                                                                                                    @Override public OWLElementHandler<?> createHandler(@Nonnull OWLXMLParserHandler handler) { return new OWLObjectPropertyAssertionAxiomElementHandler(             handler); } },
    /** DATA_PROPERTY_ASSERTION             */  PARSER_DATA_PROPERTY_ASSERTION             (DATA_PROPERTY_ASSERTION             ) { @Nonnull
                                                                                                                                    @Override public OWLElementHandler<?> createHandler(@Nonnull OWLXMLParserHandler handler) { return new OWLDataPropertyAssertionAxiomElementHandler(handler); } },
    /** NEGATIVE_OBJECT_PROPERTY_ASSERTION  */  PARSER_NEGATIVE_OBJECT_PROPERTY_ASSERTION  (NEGATIVE_OBJECT_PROPERTY_ASSERTION  ) { @Nonnull
                                                                                                                                    @Override public OWLElementHandler<?> createHandler(@Nonnull OWLXMLParserHandler handler) { return new OWLNegativeObjectPropertyAssertionAxiomElementHandler(             handler); } },
    /** NEGATIVE_DATA_PROPERTY_ASSERTION    */  PARSER_NEGATIVE_DATA_PROPERTY_ASSERTION    (NEGATIVE_DATA_PROPERTY_ASSERTION    ) { @Nonnull
                                                                                                                                    @Override public OWLElementHandler<?> createHandler(@Nonnull OWLXMLParserHandler handler) { return new OWLNegativeDataPropertyAssertionAxiomElementHandler(             handler); } },
    /** HAS_KEY                             */  PARSER_HAS_KEY                             (HAS_KEY                             ) { @Nonnull
                                                                                                                                    @Override public OWLElementHandler<?> createHandler(@Nonnull OWLXMLParserHandler handler) { return new OWLHasKeyElementHandler(handler); } },
    /** DECLARATION                         */  PARSER_DECLARATION                         (DECLARATION                         ) { @Nonnull
                                                                                                                                    @Override public OWLElementHandler<?> createHandler(@Nonnull OWLXMLParserHandler handler) { return new OWLDeclarationAxiomElementHandler(handler); } },
    /** ANNOTATION_ASSERTION                */  PARSER_ANNOTATION_ASSERTION                (ANNOTATION_ASSERTION                ) { @Nonnull
                                                                                                                                    @Override public OWLElementHandler<?> createHandler(@Nonnull OWLXMLParserHandler handler) { return new OWLAnnotationAssertionElementHandler(handler); } },
    /** ANNOTATION_PROPERTY_DOMAIN          */  PARSER_ANNOTATION_PROPERTY_DOMAIN          (ANNOTATION_PROPERTY_DOMAIN          ) { @Nonnull
                                                                                                                                    @Override public OWLElementHandler<?> createHandler(@Nonnull OWLXMLParserHandler handler) { return new OWLAnnotationPropertyDomainElementHandler(handler); } },
    /** ANNOTATION_PROPERTY_RANGE           */  PARSER_ANNOTATION_PROPERTY_RANGE           (ANNOTATION_PROPERTY_RANGE           ) { @Nonnull
                                                                                                                                    @Override public OWLElementHandler<?> createHandler(@Nonnull OWLXMLParserHandler handler) { return new OWLAnnotationPropertyRangeElementHandler(handler); } },
    /** SUB_ANNOTATION_PROPERTY_OF          */  PARSER_SUB_ANNOTATION_PROPERTY_OF          (SUB_ANNOTATION_PROPERTY_OF          ) { @Nonnull
                                                                                                                                    @Override public OWLElementHandler<?> createHandler(@Nonnull OWLXMLParserHandler handler) { return new OWLSubAnnotationPropertyOfElementHandler(handler); } },
    /** DATATYPE_DEFINITION                 */  PARSER_DATATYPE_DEFINITION                 (DATATYPE_DEFINITION                 ) { @Nonnull
                                                                                                                                    @Override public OWLElementHandler<?> createHandler(@Nonnull OWLXMLParserHandler handler) { return new OWLDatatypeDefinitionElementHandler(handler); } },
    /** IRI_ELEMENT                         */  PARSER_IRI_ELEMENT                         (IRI_ELEMENT                         ) { @Nonnull
                                                                                                                                    @Override public OWLElementHandler<?> createHandler(@Nonnull OWLXMLParserHandler handler) { return new IRIElementHandler(handler); } },
    /** ABBREVIATED_IRI_ELEMENT             */  PARSER_ABBREVIATED_IRI_ELEMENT             (ABBREVIATED_IRI_ELEMENT             ) { @Nonnull
                                                                                                                                    @Override public OWLElementHandler<?> createHandler(@Nonnull OWLXMLParserHandler handler) { return new AbbreviatedIRIElementHandler(handler); } },
    /** NODE_ID                             */  PARSER_NODE_ID                             (NODE_ID                             ),
    /** ANNOTATION_URI                      */  PARSER_ANNOTATION_URI                      (ANNOTATION_URI                      ),
    /** LABEL                               */  PARSER_LABEL                               (LABEL                               ),
    /** COMMENT                             */  PARSER_COMMENT                             (COMMENT                             ),
    /** DOCUMENTATION                       */  PARSER_DOCUMENTATION                       (DOCUMENTATION                       ),
    /** DATATYPE_FACET                      */  PARSER_DATATYPE_FACET                      (DATATYPE_FACET                      ),
    /** DATATYPE_IRI                        */  PARSER_DATATYPE_IRI                        (DATATYPE_IRI                        ),
    /** DATA_RANGE                          */  PARSER_DATA_RANGE                          (DATA_RANGE                          ),
    /** PREFIX                              */  PARSER_PREFIX                              (PREFIX                              ),
    /** NAME_ATTRIBUTE                      */  PARSER_NAME_ATTRIBUTE                      (NAME_ATTRIBUTE                      ),
    /** IRI_ATTRIBUTE                       */  PARSER_IRI_ATTRIBUTE                       (IRI_ATTRIBUTE                       ),
    /** ABBREVIATED_IRI_ATTRIBUTE           */  PARSER_ABBREVIATED_IRI_ATTRIBUTE           (ABBREVIATED_IRI_ATTRIBUTE           ),
    /** CARDINALITY_ATTRIBUTE               */  PARSER_CARDINALITY_ATTRIBUTE               (CARDINALITY_ATTRIBUTE               ),
    
    // Rules Extensions
    /** DL_SAFE_RULE                        */  PARSER_DL_SAFE_RULE                        (DL_SAFE_RULE                        ) { @Nonnull
                                                                                                                                    @Override public OWLElementHandler<?> createHandler(@Nonnull OWLXMLParserHandler handler) { return new SWRLRuleElementHandler(handler); } },
    /** BODY                                */  PARSER_BODY                                (BODY                                ) { @Nonnull
                                                                                                                                    @Override public OWLElementHandler<?> createHandler(@Nonnull OWLXMLParserHandler handler) { return new SWRLAtomListElementHandler(handler); } },
    /** HEAD                                */  PARSER_HEAD                                (HEAD                                ) { @Nonnull
                                                                                                                                    @Override public OWLElementHandler<?> createHandler(@Nonnull OWLXMLParserHandler handler) { return new SWRLAtomListElementHandler(handler); } },
    /** CLASS_ATOM                          */  PARSER_CLASS_ATOM                          (CLASS_ATOM                          ) { @Nonnull
                                                                                                                                    @Override public OWLElementHandler<?> createHandler(@Nonnull OWLXMLParserHandler handler) { return new SWRLClassAtomElementHandler(handler); } },
    /** DATA_RANGE_ATOM                     */  PARSER_DATA_RANGE_ATOM                     (DATA_RANGE_ATOM                     ) { @Nonnull
                                                                                                                                    @Override public OWLElementHandler<?> createHandler(@Nonnull OWLXMLParserHandler handler) { return new SWRLDataRangeAtomElementHandler(handler); } },
    /** OBJECT_PROPERTY_ATOM                */  PARSER_OBJECT_PROPERTY_ATOM                (OBJECT_PROPERTY_ATOM                ) { @Nonnull
                                                                                                                                    @Override public OWLElementHandler<?> createHandler(@Nonnull OWLXMLParserHandler handler) { return new SWRLObjectPropertyAtomElementHandler(handler); } },
    /** DATA_PROPERTY_ATOM                  */  PARSER_DATA_PROPERTY_ATOM                  (DATA_PROPERTY_ATOM                  ) { @Nonnull
                                                                                                                                    @Override public OWLElementHandler<?> createHandler(@Nonnull OWLXMLParserHandler handler) { return new SWRLDataPropertyAtomElementHandler(handler); } },
    /** BUILT_IN_ATOM                       */  PARSER_BUILT_IN_ATOM                       (BUILT_IN_ATOM                       ) { @Nonnull
                                                                                                                                    @Override public OWLElementHandler<?> createHandler(@Nonnull OWLXMLParserHandler handler) { return new SWRLBuiltInAtomElementHandler(handler); } },
    /** SAME_INDIVIDUAL_ATOM                */  PARSER_SAME_INDIVIDUAL_ATOM                (SAME_INDIVIDUAL_ATOM                ) { @Nonnull
                                                                                                                                    @Override public OWLElementHandler<?> createHandler(@Nonnull OWLXMLParserHandler handler) { return new SWRLSameIndividualAtomElementHandler(handler); } },
    /** DIFFERENT_INDIVIDUALS_ATOM          */  PARSER_DIFFERENT_INDIVIDUALS_ATOM          (DIFFERENT_INDIVIDUALS_ATOM          ) { @Nonnull
                                                                                                                                    @Override public OWLElementHandler<?> createHandler(@Nonnull OWLXMLParserHandler handler) { return new SWRLDifferentIndividualsAtomElementHandler(handler); } },
    /** VARIABLE                            */  PARSER_VARIABLE                            (VARIABLE                            ) { @Nonnull
                                                                                                                                    @Override public OWLElementHandler<?> createHandler(@Nonnull OWLXMLParserHandler handler) { return new SWRLVariableElementHandler(handler); } },
    /** DESCRIPTION_GRAPH_RULE              */  PARSER_DESCRIPTION_GRAPH_RULE              (DESCRIPTION_GRAPH_RULE              );
//@formatter:on
    private final IRI iri;
    private final String shortName;

    PARSER_OWLXMLVocabulary(@Nonnull OWLXMLVocabulary name) {
        iri = IRI.create(Namespaces.OWL.toString(), name.getShortForm());
        shortName = name.getShortForm();
    }

    @Override
    public IRI getIRI() {
        return iri;
    }

    /** @return short name */
    public String getShortName() {
        return shortName;
    }

    /**
     * @param handler
     *        owlxml handler
     * @return element handler
     */
    @Nullable
    public OWLElementHandler<?> createHandler(
            @SuppressWarnings("unused") OWLXMLParserHandler handler) {
        return null;
    }
}

@SuppressWarnings("unused")
abstract class OWLElementHandler<O> {

    OWLXMLParserHandler handler;
    OWLElementHandler<?> parentHandler;
    @Nullable
    StringBuilder sb;
    String elementName;
    OWLDataFactory df;

    /** @return object */
    @Nullable
    abstract O getOWLObject();

    /**
     * @throws UnloadableImportException
     *         if an import cannot be resolved
     */
    abstract void endElement();

    OWLElementHandler(@Nonnull OWLXMLParserHandler handler) {
        this.handler = handler;
        df = handler.getDataFactory();
    }

    @Nullable
    IRI getIRIFromAttribute(@Nonnull String localName, @Nonnull String value) {
        if (localName.equals(IRI_ATTRIBUTE.getShortForm())) {
            return handler.getIRI(value);
        } else if (localName.equals(ABBREVIATED_IRI_ATTRIBUTE.getShortForm())) {
            return handler.getAbbreviatedIRI(value);
        } else if (localName.equals("URI")) {
            // Legacy
            return handler.getIRI(value);
        }
        ensureAttributeNotNull(null, IRI_ATTRIBUTE.getShortForm());
        return null;
    }

    IRI getIRIFromElement(@Nonnull String elementLocalName,
            @Nonnull String textContent) {
        if (elementLocalName.equals(IRI_ELEMENT.getShortForm())) {
            return handler.getIRI(textContent.trim());
        } else if (elementLocalName.equals(ABBREVIATED_IRI_ELEMENT
                .getShortForm())) {
            return handler.getAbbreviatedIRI(textContent.trim());
        }
        throw new OWLXMLParserException(handler, elementLocalName
                + " is not an IRI element");
    }

    /**
     * @param handler
     *        element handler
     */
    void setParentHandler(OWLElementHandler<?> handler) {
        this.parentHandler = handler;
    }

    OWLElementHandler<?> getParentHandler() {
        return parentHandler;
    }

    /**
     * @param localName
     *        local attribute name
     * @param value
     *        attribute value
     */
    void attribute(String localName, String value) {}

    /**
     * @param name
     *        element name
     */
    void startElement(String name) {
        sb = null;
        elementName = name;
    }

    String getElementName() {
        return elementName;
    }

    /**
     * @param h
     *        element handler
     */
    void handleChild(AbstractOWLAxiomElementHandler h) {}

    /**
     * @param h
     *        element handler
     */
    void handleChild(AbstractClassExpressionElementHandler h) {}

    /**
     * @param h
     *        element handler
     */
    void handleChild(AbstractOWLDataRangeHandler h) {}

    /**
     * @param h
     *        element handler
     */
    void handleChild(AbstractOWLObjectPropertyElementHandler h) {}

    /**
     * @param h
     *        element handler
     */
    void handleChild(OWLDataPropertyElementHandler h) {}

    /**
     * @param h
     *        element handler
     */
    void handleChild(OWLIndividualElementHandler h) {}

    /**
     * @param h
     *        element handler
     */
    void handleChild(OWLLiteralElementHandler h) {}

    /**
     * @param h
     *        element handler
     */
    void handleChild(OWLAnnotationElementHandler h) {}

    /**
     * @param h
     *        element handler
     */
    void handleChild(OWLSubObjectPropertyChainElementHandler h) {}

    /**
     * @param h
     *        element handler
     */
    void handleChild(OWLDatatypeFacetRestrictionElementHandler h) {}

    /**
     * @param h
     *        element handler
     */
    void handleChild(OWLAnnotationPropertyElementHandler h) {}

    /**
     * @param h
     *        element handler
     */
    void handleChild(OWLAnonymousIndividualElementHandler h) {}

    /**
     * @param h
     *        element handler
     */
    void handleChild(AbstractIRIElementHandler h) {}

    /**
     * @param h
     *        element handler
     */
    void handleChild(SWRLVariableElementHandler h) {}

    /**
     * @param h
     *        element handler
     */
    void handleChild(SWRLAtomElementHandler h) {}

    /**
     * @param h
     *        element handler
     */
    void handleChild(SWRLAtomListElementHandler h) {}

    void ensureNotNull(@Nullable Object element, String message) {
        if (element == null) {
            throw new OWLXMLParserElementNotFoundException(handler, message);
        }
    }

    void ensureAttributeNotNull(@Nullable Object element, String message) {
        if (element == null) {
            throw new OWLXMLParserAttributeNotFoundException(handler, message);
        }
    }

    /**
     * @param chars
     *        chars to handle
     * @param start
     *        start index
     * @param length
     *        end index
     */
    void handleChars(char[] chars, int start, int length) {
        if (isTextContentPossible()) {
            if (sb == null) {
                sb = new StringBuilder();
            }
            sb.append(chars, start, length);
        }
    }

    /** @return text handled */
    @Nonnull
    String getText() {
        if (sb == null) {
            return "";
        } else {
            return sb.toString();
        }
    }

    /** @return true if text can be contained */
    boolean isTextContentPossible() {
        return false;
    }
}

abstract class AbstractClassExpressionElementHandler extends
        OWLElementHandler<OWLClassExpression> {

    OWLClassExpression desc;

    AbstractClassExpressionElementHandler(@Nonnull OWLXMLParserHandler handler) {
        super(handler);
    }

    @Override
    void endElement() {
        endClassExpressionElement();
        getParentHandler().handleChild(this);
    }

    abstract void endClassExpressionElement();

    void setClassExpression(OWLClassExpression desc) {
        this.desc = desc;
    }

    @Override
    OWLClassExpression getOWLObject() {
        return desc;
    }
}

abstract class AbstractClassExpressionFillerRestriction extends
        AbstractObjectRestrictionElementHandler<OWLClassExpression> {

    AbstractClassExpressionFillerRestriction(
            @Nonnull OWLXMLParserHandler handler) {
        super(handler);
    }

    @Override
    void handleChild(@Nonnull AbstractClassExpressionElementHandler h) {
        setFiller(h.getOWLObject());
    }
}

abstract class AbstractClassExpressionOperandAxiomElementHandler extends
        AbstractOperandAxiomElementHandler<OWLClassExpression> {

    AbstractClassExpressionOperandAxiomElementHandler(
            @Nonnull OWLXMLParserHandler handler) {
        super(handler);
    }

    @Override
    void handleChild(@Nonnull AbstractClassExpressionElementHandler h) {
        addOperand(h.getOWLObject());
    }
}

abstract class AbstractDataCardinalityRestrictionElementHandler extends
        AbstractDataRangeFillerRestrictionElementHandler {

    int cardinality;

    AbstractDataCardinalityRestrictionElementHandler(
            @Nonnull OWLXMLParserHandler handler) {
        super(handler);
    }

    @Override
    void attribute(@Nonnull String localName, String value) {
        if (localName.equals("cardinality")) {
            cardinality = Integer.parseInt(value);
        }
    }

    @Override
    void startElement(String name) {
        super.startElement(name);
        setFiller(df.getTopDatatype());
    }

    int getCardinality() {
        return cardinality;
    }
}

abstract class AbstractDataRangeFillerRestrictionElementHandler extends
        AbstractDataRestrictionElementHandler<OWLDataRange> {

    AbstractDataRangeFillerRestrictionElementHandler(
            @Nonnull OWLXMLParserHandler handler) {
        super(handler);
    }

    @Override
    void handleChild(@Nonnull AbstractOWLDataRangeHandler h) {
        setFiller(h.getOWLObject());
    }
}

abstract class AbstractNaryBooleanClassExpressionElementHandler extends
        AbstractClassExpressionElementHandler {

    Set<OWLClassExpression> operands;

    AbstractNaryBooleanClassExpressionElementHandler(
            @Nonnull OWLXMLParserHandler handler) {
        super(handler);
        operands = new HashSet<OWLClassExpression>();
    }

    @Override
    void handleChild(@Nonnull AbstractClassExpressionElementHandler h) {
        operands.add(h.getOWLObject());
    }

    @Override
    void endClassExpressionElement() {
        if (operands.size() >= 2) {
            setClassExpression(createClassExpression(operands));
        } else if (operands.size() == 1) {
            setClassExpression(operands.iterator().next());
        } else {
            String template = "Found zero child elements of an %s element. At least 2 class expression elements are required as child elements of %s elements";
            ensureNotNull(null,
                    String.format(template, getElementName(), getElementName()));
        }
    }

    abstract OWLClassExpression createClassExpression(
            Set<OWLClassExpression> expressions);
}

abstract class AbstractIRIElementHandler extends OWLElementHandler<IRI> {

    AbstractIRIElementHandler(@Nonnull OWLXMLParserHandler handler) {
        super(handler);
    }
}

abstract class AbstractDataRestrictionElementHandler<F extends OWLObject>
        extends AbstractRestrictionElementHandler<OWLDataPropertyExpression, F> {

    AbstractDataRestrictionElementHandler(@Nonnull OWLXMLParserHandler handler) {
        super(handler);
    }

    @Override
    void handleChild(@Nonnull OWLDataPropertyElementHandler h) {
        setProperty(h.getOWLObject());
    }
}

abstract class AbstractObjectRestrictionElementHandler<F extends OWLObject>
        extends
        AbstractRestrictionElementHandler<OWLObjectPropertyExpression, F> {

    AbstractObjectRestrictionElementHandler(@Nonnull OWLXMLParserHandler handler) {
        super(handler);
    }

    @Override
    void handleChild(@Nonnull AbstractOWLObjectPropertyElementHandler h) {
        setProperty(h.getOWLObject());
    }
}

abstract class AbstractOperandAxiomElementHandler<O extends OWLObject> extends
        AbstractOWLAxiomElementHandler {

    @Nonnull
    Set<O> operands = new HashSet<O>();

    AbstractOperandAxiomElementHandler(@Nonnull OWLXMLParserHandler handler) {
        super(handler);
    }

    @Override
    void startElement(String name) {
        super.startElement(name);
        operands.clear();
    }

    @Nonnull
    Set<O> getOperands() {
        return operands;
    }

    void addOperand(O operand) {
        operands.add(operand);
    }
}

abstract class AbstractOWLAssertionAxiomElementHandler<P extends OWLPropertyExpression, O extends OWLObject>
        extends AbstractOWLAxiomElementHandler {

    OWLIndividual subject;
    P property;
    O object;

    AbstractOWLAssertionAxiomElementHandler(@Nonnull OWLXMLParserHandler handler) {
        super(handler);
    }

    OWLIndividual getSubject() {
        return subject;
    }

    P getProperty() {
        return property;
    }

    O getObject() {
        return object;
    }

    void setSubject(OWLIndividual subject) {
        this.subject = subject;
    }

    void setProperty(P property) {
        this.property = property;
    }

    void setObject(O object) {
        this.object = object;
    }
}

abstract class AbstractOWLAxiomElementHandler extends
        OWLElementHandler<OWLAxiom> {

    OWLAxiom axiom;
    @Nonnull
    Set<OWLAnnotation> annotations = new HashSet<OWLAnnotation>();

    AbstractOWLAxiomElementHandler(@Nonnull OWLXMLParserHandler handler) {
        super(handler);
    }

    @Override
    OWLAxiom getOWLObject() {
        return axiom;
    }

    void setAxiom(OWLAxiom axiom) {
        this.axiom = axiom;
    }

    @Override
    void startElement(String name) {
        annotations.clear();
    }

    @Override
    void handleChild(@Nonnull OWLAnnotationElementHandler h) {
        annotations.add(h.getOWLObject());
    }

    @Override
    void endElement() {
        setAxiom(createAxiom());
        getParentHandler().handleChild(this);
    }

    @Nonnull
    Set<OWLAnnotation> getAnnotations() {
        return annotations;
    }

    abstract OWLAxiom createAxiom();
}

abstract class AbstractOWLDataPropertyAssertionAxiomElementHandler
        extends
        AbstractOWLAssertionAxiomElementHandler<OWLDataPropertyExpression, OWLLiteral> {

    AbstractOWLDataPropertyAssertionAxiomElementHandler(
            @Nonnull OWLXMLParserHandler handler) {
        super(handler);
    }

    @Override
    void handleChild(@Nonnull OWLAnonymousIndividualElementHandler h) {
        setSubject(h.getOWLObject());
    }

    @Override
    void handleChild(@Nonnull OWLIndividualElementHandler h) {
        setSubject(h.getOWLObject());
    }

    @Override
    void handleChild(@Nonnull OWLDataPropertyElementHandler h) {
        setProperty(h.getOWLObject());
    }

    @Override
    void handleChild(@Nonnull OWLLiteralElementHandler h) {
        setObject(h.getOWLObject());
    }
}

abstract class AbstractOWLDataPropertyOperandAxiomElementHandler extends
        AbstractOperandAxiomElementHandler<OWLDataPropertyExpression> {

    AbstractOWLDataPropertyOperandAxiomElementHandler(
            @Nonnull OWLXMLParserHandler handler) {
        super(handler);
    }

    @Override
    void handleChild(@Nonnull OWLDataPropertyElementHandler h) {
        addOperand(h.getOWLObject());
    }
}

abstract class AbstractOWLDataRangeHandler extends
        OWLElementHandler<OWLDataRange> {

    OWLDataRange dataRange;

    AbstractOWLDataRangeHandler(@Nonnull OWLXMLParserHandler handler) {
        super(handler);
    }

    void setDataRange(OWLDataRange dataRange) {
        this.dataRange = dataRange;
    }

    @Override
    OWLDataRange getOWLObject() {
        return dataRange;
    }

    @Override
    void endElement() {
        endDataRangeElement();
        getParentHandler().handleChild(this);
    }

    abstract void endDataRangeElement();
}

abstract class AbstractOWLIndividualOperandAxiomElementHandler extends
        AbstractOperandAxiomElementHandler<OWLIndividual> {

    AbstractOWLIndividualOperandAxiomElementHandler(
            @Nonnull OWLXMLParserHandler handler) {
        super(handler);
    }

    @Override
    void handleChild(@Nonnull OWLIndividualElementHandler h) {
        addOperand(h.getOWLObject());
    }

    @Override
    void handleChild(@Nonnull OWLAnonymousIndividualElementHandler h) {
        addOperand(h.getOWLObject());
    }
}

abstract class AbstractOWLObjectCardinalityElementHandler extends
        AbstractClassExpressionFillerRestriction {

    int cardinality;

    AbstractOWLObjectCardinalityElementHandler(
            @Nonnull OWLXMLParserHandler handler) {
        super(handler);
    }

    @Override
    void startElement(String name) {
        super.startElement(name);
        setFiller(df.getOWLThing());
    }

    @Override
    void attribute(@Nonnull String localName, String value) {
        if (localName.equals("cardinality")) {
            cardinality = Integer.parseInt(value);
        }
    }

    @Override
    OWLClassExpression createRestriction() {
        return createCardinalityRestriction();
    }

    abstract OWLClassExpression createCardinalityRestriction();

    int getCardinality() {
        return cardinality;
    }
}

abstract class AbstractOWLObjectPropertyAssertionAxiomElementHandler
        extends
        AbstractOWLAssertionAxiomElementHandler<OWLObjectPropertyExpression, OWLIndividual> {

    AbstractOWLObjectPropertyAssertionAxiomElementHandler(
            @Nonnull OWLXMLParserHandler handler) {
        super(handler);
    }

    @Override
    void handleChild(@Nonnull OWLAnonymousIndividualElementHandler h) {
        if (getSubject() == null) {
            setSubject(h.getOWLObject());
        } else if (getObject() == null) {
            setObject(h.getOWLObject());
        }
    }

    @Override
    void handleChild(@Nonnull OWLIndividualElementHandler h) {
        if (getSubject() == null) {
            setSubject(h.getOWLObject());
        } else if (getObject() == null) {
            setObject(h.getOWLObject());
        }
    }

    @Override
    void handleChild(@Nonnull AbstractOWLObjectPropertyElementHandler h) {
        setProperty(h.getOWLObject());
    }
}

abstract class AbstractOWLObjectPropertyCharacteristicAxiomElementHandler
        extends
        AbstractOWLPropertyCharacteristicAxiomElementHandler<OWLObjectPropertyExpression> {

    AbstractOWLObjectPropertyCharacteristicAxiomElementHandler(
            @Nonnull OWLXMLParserHandler handler) {
        super(handler);
    }

    @Override
    void handleChild(@Nonnull AbstractOWLObjectPropertyElementHandler h) {
        setProperty(h.getOWLObject());
    }
}

abstract class AbstractOWLObjectPropertyElementHandler extends
        OWLElementHandler<OWLObjectPropertyExpression> {

    OWLObjectPropertyExpression property;

    AbstractOWLObjectPropertyElementHandler(@Nonnull OWLXMLParserHandler handler) {
        super(handler);
    }

    @Override
    void endElement() {
        endObjectPropertyElement();
        getParentHandler().handleChild(this);
    }

    void setOWLObjectPropertyExpression(OWLObjectPropertyExpression prop) {
        property = prop;
    }

    @Override
    OWLObjectPropertyExpression getOWLObject() {
        return property;
    }

    abstract void endObjectPropertyElement();
}

abstract class AbstractOWLObjectPropertyOperandAxiomElementHandler extends
        AbstractOperandAxiomElementHandler<OWLObjectPropertyExpression> {

    AbstractOWLObjectPropertyOperandAxiomElementHandler(
            @Nonnull OWLXMLParserHandler handler) {
        super(handler);
    }

    @Override
    void handleChild(@Nonnull AbstractOWLObjectPropertyElementHandler h) {
        addOperand(h.getOWLObject());
    }
}

abstract class AbstractOWLPropertyCharacteristicAxiomElementHandler<P extends OWLObject>
        extends AbstractOWLAxiomElementHandler {

    P property;

    AbstractOWLPropertyCharacteristicAxiomElementHandler(
            @Nonnull OWLXMLParserHandler handler) {
        super(handler);
    }

    void setProperty(P property) {
        this.property = property;
    }

    P getProperty() {
        return property;
    }

    @Override
    OWLAxiom createAxiom() {
        ensureNotNull(property, "property element");
        return createPropertyCharacteristicAxiom();
    }

    abstract OWLAxiom createPropertyCharacteristicAxiom();
}

abstract class AbstractRestrictionElementHandler<P extends OWLPropertyExpression, F extends OWLObject>
        extends AbstractClassExpressionElementHandler {

    P property;
    F filler;

    AbstractRestrictionElementHandler(@Nonnull OWLXMLParserHandler handler) {
        super(handler);
    }

    void setProperty(P prop) {
        property = prop;
    }

    P getProperty() {
        return property;
    }

    F getFiller() {
        return filler;
    }

    void setFiller(F filler) {
        this.filler = filler;
    }

    @Override
    void endClassExpressionElement() {
        setClassExpression(createRestriction());
    }

    abstract OWLClassExpression createRestriction();
}

class AbbreviatedIRIElementHandler extends AbstractIRIElementHandler {

    AbbreviatedIRIElementHandler(@Nonnull OWLXMLParserHandler handler) {
        super(handler);
    }

    IRI iri;

    @Override
    boolean isTextContentPossible() {
        return true;
    }

    @Override
    IRI getOWLObject() {
        return iri;
    }

    @Override
    void endElement() {
        String iriText = getText().trim();
        iri = handler.getAbbreviatedIRI(iriText);
        getParentHandler().handleChild(this);
    }
}

class IRIElementHandler extends AbstractIRIElementHandler {

    IRIElementHandler(@Nonnull OWLXMLParserHandler handler) {
        super(handler);
    }

    IRI iri;

    @Override
    boolean isTextContentPossible() {
        return true;
    }

    @Override
    IRI getOWLObject() {
        return iri;
    }

    @Override
    void endElement() {
        String iriText = getText().trim();
        iri = handler.getIRI(iriText);
        getParentHandler().handleChild(this);
    }
}

class OWLUnionOfElementHandler extends OWLElementHandler<OWLClassExpression> {

    OWLUnionOfElementHandler(@Nonnull OWLXMLParserHandler handler) {
        super(handler);
    }

    @Override
    void handleChild(AbstractClassExpressionElementHandler h) {
        // We simply pass on to our parent, which MUST be an OWLDisjointUnionOf
        getParentHandler().handleChild(h);
    }

    @Override
    void endElement() {}

    @Nonnull
    @Override
    OWLClassExpression getOWLObject() {
        throw new OWLRuntimeException(
                "getOWLObject should not be called on OWLUnionOfElementHandler");
    }
}

class LegacyEntityAnnotationElementHandler extends
        AbstractOWLAxiomElementHandler {

    @Nullable
    OWLEntity entity;
    @Nullable
    OWLAnnotation annotation;

    LegacyEntityAnnotationElementHandler(@Nonnull OWLXMLParserHandler handler) {
        super(handler);
    }

    @Override
    OWLAxiom createAxiom() {
        OWLAnnotationAssertionAxiom toReturn = df
                .getOWLAnnotationAssertionAxiom(annotation.getProperty(),
                        entity.getIRI(), annotation.getValue());
        annotation = null;
        entity = null;
        return toReturn;
    }

    @Override
    void handleChild(@Nonnull AbstractClassExpressionElementHandler h) {
        entity = h.getOWLObject().asOWLClass();
    }

    @Override
    void handleChild(@Nonnull OWLDataPropertyElementHandler h) {
        entity = h.getOWLObject().asOWLDataProperty();
    }

    @Override
    void handleChild(@Nonnull OWLIndividualElementHandler h) {
        entity = h.getOWLObject();
    }

    @Override
    void handleChild(@Nonnull AbstractOWLObjectPropertyElementHandler h) {
        entity = h.getOWLObject().asOWLObjectProperty();
    }

    @Override
    void handleChild(@Nonnull OWLAnnotationElementHandler h) {
        if (entity == null) {
            super.handleChild(h);
        } else {
            annotation = h.getOWLObject();
        }
    }
}

class OWLAnnotationAssertionElementHandler extends
        AbstractOWLAxiomElementHandler {

    OWLAnnotationAssertionElementHandler(@Nonnull OWLXMLParserHandler handler) {
        super(handler);
    }

    @Nullable
    OWLAnnotationSubject subject = null;
    @Nullable
    OWLAnnotationValue object = null;
    @Nullable
    OWLAnnotationProperty property = null;

    @Override
    void handleChild(@Nonnull AbstractIRIElementHandler h) {
        if (subject == null) {
            subject = h.getOWLObject();
        } else {
            object = h.getOWLObject();
        }
    }

    @Override
    void handleChild(@Nonnull OWLAnonymousIndividualElementHandler h) {
        if (subject == null) {
            subject = h.getOWLObject();
        } else {
            object = h.getOWLObject();
        }
    }

    @Override
    void handleChild(@Nonnull OWLAnnotationPropertyElementHandler h) {
        property = h.getOWLObject();
    }

    @Override
    void handleChild(@Nonnull OWLLiteralElementHandler h) {
        object = h.getOWLObject();
    }

    @Override
    OWLAxiom createAxiom() {
        return df.getOWLAnnotationAssertionAxiom(property, subject, object,
                getAnnotations());
    }
}

class OWLAnnotationElementHandler extends OWLElementHandler<OWLAnnotation> {

    @Nonnull
    Set<OWLAnnotation> annotations = new HashSet<OWLAnnotation>();
    OWLAnnotationProperty property;
    @Nullable
    OWLAnnotationValue object;

    OWLAnnotationElementHandler(@Nonnull OWLXMLParserHandler handler) {
        super(handler);
    }

    @Override
    void startElement(String name) {
        super.startElement(name);
    }

    @Override
    void endElement() {
        getParentHandler().handleChild(this);
    }

    @Override
    void attribute(@Nonnull String localName, @Nonnull String value) {
        super.attribute(localName, value);
        // Legacy Handling
        if (localName.equals(ANNOTATION_URI.getShortForm())) {
            IRI iri = handler.getIRI(value);
            property = df.getOWLAnnotationProperty(iri);
        }
    }

    @Override
    void handleChild(@Nonnull OWLAnnotationElementHandler h) {
        annotations.add(h.getOWLObject());
    }

    @Override
    void handleChild(@Nonnull OWLAnonymousIndividualElementHandler h) {
        object = h.getOWLObject();
    }

    @Override
    void handleChild(@Nonnull OWLLiteralElementHandler h) {
        object = h.getOWLObject();
    }

    @Override
    void handleChild(@Nonnull OWLAnnotationPropertyElementHandler h) {
        property = h.getOWLObject();
    }

    @Override
    void handleChild(@Nonnull AbstractIRIElementHandler h) {
        object = h.getOWLObject();
    }

    @Override
    OWLAnnotation getOWLObject() {
        return df.getOWLAnnotation(property, object, annotations);
    }

    @Override
    boolean isTextContentPossible() {
        return false;
    }
}

class OWLAnnotationPropertyDomainElementHandler extends
        AbstractOWLAxiomElementHandler {

    @Nullable
    IRI domain;
    OWLAnnotationProperty property;

    OWLAnnotationPropertyDomainElementHandler(
            @Nonnull OWLXMLParserHandler handler) {
        super(handler);
    }

    @Override
    void handleChild(@Nonnull AbstractIRIElementHandler h) {
        domain = h.getOWLObject();
    }

    @Override
    void handleChild(@Nonnull OWLAnnotationPropertyElementHandler h) {
        property = h.getOWLObject();
    }

    @Override
    OWLAxiom createAxiom() {
        ensureNotNull(property, "Expected annotation property element");
        ensureNotNull(domain, "Expected iri element");
        return df.getOWLAnnotationPropertyDomainAxiom(property, domain,
                getAnnotations());
    }
}

class OWLAnnotationPropertyElementHandler extends
        OWLElementHandler<OWLAnnotationProperty> {

    OWLAnnotationProperty prop;
    @Nullable
    IRI iri;

    OWLAnnotationPropertyElementHandler(@Nonnull OWLXMLParserHandler handler) {
        super(handler);
    }

    @Override
    OWLAnnotationProperty getOWLObject() {
        return prop;
    }

    @Override
    void attribute(@Nonnull String localName, @Nonnull String value) {
        iri = getIRIFromAttribute(localName, value);
    }

    @Override
    void endElement() {
        prop = df.getOWLAnnotationProperty(iri);
        getParentHandler().handleChild(this);
    }
}

class OWLAnnotationPropertyRangeElementHandler extends
        AbstractOWLAxiomElementHandler {

    @Nullable
    IRI range;
    OWLAnnotationProperty property;

    OWLAnnotationPropertyRangeElementHandler(
            @Nonnull OWLXMLParserHandler handler) {
        super(handler);
    }

    @Override
    void handleChild(@Nonnull AbstractIRIElementHandler h) {
        range = h.getOWLObject();
    }

    @Override
    void handleChild(@Nonnull OWLAnnotationPropertyElementHandler h) {
        property = h.getOWLObject();
    }

    @Override
    OWLAxiom createAxiom() {
        ensureNotNull(property, "Expected annotation property element");
        ensureNotNull(range, "Expected IRI element");
        return df.getOWLAnnotationPropertyRangeAxiom(property, range,
                getAnnotations());
    }
}

class OWLAnonymousIndividualElementHandler extends
        OWLElementHandler<OWLAnonymousIndividual> {

    OWLAnonymousIndividual ind;

    OWLAnonymousIndividualElementHandler(@Nonnull OWLXMLParserHandler handler) {
        super(handler);
    }

    @Override
    OWLAnonymousIndividual getOWLObject() {
        return ind;
    }

    @Override
    void attribute(@Nonnull String localName, @Nonnull String value) {
        if (localName.equals(NODE_ID.getShortForm())) {
            ind = df.getOWLAnonymousIndividual(value.trim());
        } else {
            super.attribute(localName, value);
        }
    }

    @Override
    void endElement() {
        getParentHandler().handleChild(this);
    }
}

class OWLAsymmetricObjectPropertyAxiomElementHandler extends
        AbstractOWLObjectPropertyCharacteristicAxiomElementHandler {

    OWLAsymmetricObjectPropertyAxiomElementHandler(
            @Nonnull OWLXMLParserHandler handler) {
        super(handler);
    }

    @Override
    OWLAxiom createPropertyCharacteristicAxiom() {
        return df.getOWLAsymmetricObjectPropertyAxiom(getProperty());
    }
}

class OWLAsymmetricObjectPropertyElementHandler extends
        AbstractOWLObjectPropertyCharacteristicAxiomElementHandler {

    OWLAsymmetricObjectPropertyElementHandler(
            @Nonnull OWLXMLParserHandler handler) {
        super(handler);
    }

    @Override
    OWLAxiom createPropertyCharacteristicAxiom() {
        return df.getOWLAsymmetricObjectPropertyAxiom(getProperty(),
                getAnnotations());
    }
}

class OWLClassAssertionAxiomElementHandler extends
        AbstractOWLAxiomElementHandler {

    OWLIndividual individual;
    OWLClassExpression classExpression;

    OWLClassAssertionAxiomElementHandler(@Nonnull OWLXMLParserHandler handler) {
        super(handler);
    }

    @Override
    void handleChild(@Nonnull AbstractClassExpressionElementHandler h) {
        classExpression = h.getOWLObject();
    }

    @Override
    void handleChild(@Nonnull OWLIndividualElementHandler h) {
        individual = h.getOWLObject();
    }

    @Override
    void handleChild(@Nonnull OWLAnonymousIndividualElementHandler h) {
        individual = h.getOWLObject();
    }

    @Override
    OWLAxiom createAxiom() {
        ensureNotNull(individual, "individual element");
        ensureNotNull(classExpression, "classExpression kind element");
        return df.getOWLClassAssertionAxiom(classExpression, individual,
                getAnnotations());
    }
}

class OWLClassElementHandler extends AbstractClassExpressionElementHandler {

    @Nullable
    IRI iri;

    OWLClassElementHandler(@Nonnull OWLXMLParserHandler handler) {
        super(handler);
    }

    @Override
    void attribute(@Nonnull String localName, @Nonnull String value) {
        iri = getIRIFromAttribute(localName, value);
    }

    @Override
    void endClassExpressionElement() {
        ensureAttributeNotNull(iri, "IRI");
        setClassExpression(df.getOWLClass(iri));
    }
}

class OWLDataAllValuesFromElementHandler extends
        AbstractDataRangeFillerRestrictionElementHandler {

    OWLDataAllValuesFromElementHandler(@Nonnull OWLXMLParserHandler handler) {
        super(handler);
    }

    @Override
    OWLClassExpression createRestriction() {
        return df.getOWLDataAllValuesFrom(getProperty(), getFiller());
    }
}

class OWLDataComplementOfElementHandler extends AbstractOWLDataRangeHandler {

    OWLDataRange operand;

    OWLDataComplementOfElementHandler(@Nonnull OWLXMLParserHandler handler) {
        super(handler);
    }

    @Override
    void handleChild(@Nonnull AbstractOWLDataRangeHandler h) {
        operand = h.getOWLObject();
    }

    @Override
    void endDataRangeElement() {
        ensureNotNull(operand, "data range element");
        setDataRange(df.getOWLDataComplementOf(operand));
    }
}

class OWLDataExactCardinalityElementHandler extends
        AbstractDataCardinalityRestrictionElementHandler {

    OWLDataExactCardinalityElementHandler(@Nonnull OWLXMLParserHandler handler) {
        super(handler);
    }

    @Override
    OWLClassExpression createRestriction() {
        return df.getOWLDataExactCardinality(getCardinality(), getProperty(),
                getFiller());
    }
}

class OWLDataHasValueElementHandler extends
        AbstractDataRestrictionElementHandler<OWLLiteral> {

    OWLDataHasValueElementHandler(@Nonnull OWLXMLParserHandler handler) {
        super(handler);
    }

    @Override
    void handleChild(@Nonnull OWLLiteralElementHandler h) {
        setFiller(h.getOWLObject());
    }

    @Override
    OWLClassExpression createRestriction() {
        return df.getOWLDataHasValue(getProperty(), getFiller());
    }
}

class OWLDataIntersectionOfElementHandler extends AbstractOWLDataRangeHandler {

    @Nonnull
    Set<OWLDataRange> dataRanges = new HashSet<OWLDataRange>();

    OWLDataIntersectionOfElementHandler(@Nonnull OWLXMLParserHandler handler) {
        super(handler);
    }

    @Override
    void handleChild(@Nonnull AbstractOWLDataRangeHandler h) {
        dataRanges.add(h.getOWLObject());
    }

    @Override
    void endDataRangeElement() {
        setDataRange(df.getOWLDataIntersectionOf(dataRanges));
    }
}

class OWLDataMaxCardinalityElementHandler extends
        AbstractDataCardinalityRestrictionElementHandler {

    OWLDataMaxCardinalityElementHandler(@Nonnull OWLXMLParserHandler handler) {
        super(handler);
    }

    @Override
    OWLClassExpression createRestriction() {
        return df.getOWLDataMaxCardinality(getCardinality(), getProperty(),
                getFiller());
    }
}

class OWLDataMinCardinalityElementHandler extends
        AbstractDataCardinalityRestrictionElementHandler {

    OWLDataMinCardinalityElementHandler(@Nonnull OWLXMLParserHandler handler) {
        super(handler);
    }

    @Override
    OWLClassExpression createRestriction() {
        return df.getOWLDataMinCardinality(getCardinality(), getProperty(),
                getFiller());
    }
}

class OWLDataOneOfElementHandler extends AbstractOWLDataRangeHandler {

    Set<OWLLiteral> constants;

    OWLDataOneOfElementHandler(@Nonnull OWLXMLParserHandler handler) {
        super(handler);
        constants = new HashSet<OWLLiteral>();
    }

    @Override
    void handleChild(@Nonnull OWLLiteralElementHandler h) {
        constants.add(h.getOWLObject());
    }

    @Override
    void endDataRangeElement() {
        if (constants.isEmpty()) {
            ensureNotNull(null, "data oneOf element");
        }
        setDataRange(df.getOWLDataOneOf(constants));
    }
}

class OWLDataPropertyAssertionAxiomElementHandler extends
        AbstractOWLDataPropertyAssertionAxiomElementHandler {

    OWLDataPropertyAssertionAxiomElementHandler(
            @Nonnull OWLXMLParserHandler handler) {
        super(handler);
    }

    @Override
    OWLAxiom createAxiom() {
        return df.getOWLDataPropertyAssertionAxiom(getProperty(), getSubject(),
                getObject(), getAnnotations());
    }
}

class OWLDataPropertyDomainAxiomElementHandler extends
        AbstractOWLAxiomElementHandler {

    OWLClassExpression domain;
    OWLDataPropertyExpression property;

    OWLDataPropertyDomainAxiomElementHandler(
            @Nonnull OWLXMLParserHandler handler) {
        super(handler);
    }

    @Override
    void handleChild(@Nonnull AbstractClassExpressionElementHandler h) {
        domain = h.getOWLObject();
    }

    @Override
    void handleChild(@Nonnull OWLDataPropertyElementHandler h) {
        property = h.getOWLObject();
    }

    @Override
    OWLAxiom createAxiom() {
        ensureNotNull(property, "data property element");
        ensureNotNull(domain, "class expression element");
        return df.getOWLDataPropertyDomainAxiom(property, domain,
                getAnnotations());
    }
}

class OWLDataPropertyElementHandler extends
        OWLElementHandler<OWLDataPropertyExpression> {

    OWLDataPropertyExpression prop;
    @Nullable
    IRI iri;

    OWLDataPropertyElementHandler(@Nonnull OWLXMLParserHandler handler) {
        super(handler);
    }

    @Override
    OWLDataPropertyExpression getOWLObject() {
        return prop;
    }

    @Override
    void attribute(@Nonnull String localName, @Nonnull String value) {
        iri = getIRIFromAttribute(localName, value);
    }

    @Override
    void endElement() {
        prop = df.getOWLDataProperty(iri);
        getParentHandler().handleChild(this);
    }
}

class OWLDataPropertyRangeAxiomElementHandler extends
        AbstractOWLAxiomElementHandler {

    OWLDataPropertyExpression property;
    OWLDataRange range;

    OWLDataPropertyRangeAxiomElementHandler(@Nonnull OWLXMLParserHandler handler) {
        super(handler);
    }

    @Override
    void handleChild(@Nonnull AbstractOWLDataRangeHandler h) {
        range = h.getOWLObject();
    }

    @Override
    void handleChild(@Nonnull OWLDataPropertyElementHandler h) {
        property = h.getOWLObject();
    }

    @Override
    OWLAxiom createAxiom() {
        ensureNotNull(property, "data property element");
        ensureNotNull(range, "data range element");
        return df.getOWLDataPropertyRangeAxiom(property, range,
                getAnnotations());
    }
}

class OWLDataRestrictionElementHandler extends AbstractOWLDataRangeHandler {

    OWLLiteral constant;
    IRI facetIRI;

    OWLDataRestrictionElementHandler(@Nonnull OWLXMLParserHandler handler) {
        super(handler);
    }

    @Override
    void handleChild(@Nonnull AbstractOWLDataRangeHandler h) {
        dataRange = h.getOWLObject();
    }

    @Override
    void handleChild(@Nonnull OWLLiteralElementHandler h) {
        constant = h.getOWLObject();
    }

    @Override
    void attribute(@Nonnull String localName, @Nonnull String value) {
        super.attribute(localName, value);
        if (localName.equals("facet")) {
            facetIRI = handler.getIRI(value);
        }
    }

    @Override
    void endDataRangeElement() {
        ensureNotNull(dataRange, "data range element");
        ensureNotNull(constant, "typed constant element");
        setDataRange(df.getOWLDatatypeRestriction((OWLDatatype) dataRange,
                OWLFacet.getFacet(facetIRI), constant));
    }
}

class OWLDataSomeValuesFromElementHandler extends
        AbstractDataRangeFillerRestrictionElementHandler {

    OWLDataSomeValuesFromElementHandler(@Nonnull OWLXMLParserHandler handler) {
        super(handler);
    }

    @Override
    OWLClassExpression createRestriction() {
        return df.getOWLDataSomeValuesFrom(getProperty(), getFiller());
    }
}

class OWLDataUnionOfElementHandler extends AbstractOWLDataRangeHandler {

    @Nonnull
    Set<OWLDataRange> dataRanges = new HashSet<OWLDataRange>();

    OWLDataUnionOfElementHandler(@Nonnull OWLXMLParserHandler handler) {
        super(handler);
    }

    @Override
    void handleChild(@Nonnull AbstractOWLDataRangeHandler h) {
        dataRanges.add(h.getOWLObject());
    }

    @Override
    void endDataRangeElement() {
        setDataRange(df.getOWLDataUnionOf(dataRanges));
    }
}

class OWLDatatypeDefinitionElementHandler extends
        AbstractOWLAxiomElementHandler {

    OWLDatatype datatype;
    OWLDataRange dataRange;

    OWLDatatypeDefinitionElementHandler(@Nonnull OWLXMLParserHandler handler) {
        super(handler);
    }

    @Override
    void handleChild(@Nonnull AbstractOWLDataRangeHandler h) {
        OWLDataRange handledDataRange = h.getOWLObject();
        if (handledDataRange.isDatatype() && datatype == null) {
            datatype = handledDataRange.asOWLDatatype();
        } else {
            dataRange = handledDataRange;
        }
    }

    @Override
    OWLAxiom createAxiom() {
        return df.getOWLDatatypeDefinitionAxiom(datatype, dataRange,
                getAnnotations());
    }
}

class OWLDatatypeElementHandler extends AbstractOWLDataRangeHandler {

    @Nullable
    IRI iri;

    OWLDatatypeElementHandler(@Nonnull OWLXMLParserHandler handler) {
        super(handler);
    }

    @Override
    void attribute(@Nonnull String localName, @Nonnull String value) {
        iri = getIRIFromAttribute(localName, value);
    }

    @Override
    void endDataRangeElement() {
        ensureAttributeNotNull(iri, "IRI");
        setDataRange(df.getOWLDatatype(iri));
    }
}

class OWLDatatypeFacetRestrictionElementHandler extends
        OWLElementHandler<OWLFacetRestriction> {

    OWLFacet facet;
    OWLLiteral constant;

    OWLDatatypeFacetRestrictionElementHandler(
            @Nonnull OWLXMLParserHandler handler) {
        super(handler);
    }

    @Override
    void handleChild(@Nonnull OWLLiteralElementHandler h) {
        constant = h.getOWLObject();
    }

    @Override
    void attribute(@Nonnull String localName, String value) {
        if (localName.equals("facet")) {
            facet = OWLFacet.getFacet(IRI.create(value));
        }
    }

    @Override
    void endElement() {
        getParentHandler().handleChild(this);
    }

    @Override
    OWLFacetRestriction getOWLObject() {
        return df.getOWLFacetRestriction(facet, constant);
    }
}

class OWLDatatypeRestrictionElementHandler extends AbstractOWLDataRangeHandler {

    OWLDatatype restrictedDataRange;
    Set<OWLFacetRestriction> facetRestrictions;

    OWLDatatypeRestrictionElementHandler(@Nonnull OWLXMLParserHandler handler) {
        super(handler);
        facetRestrictions = new HashSet<OWLFacetRestriction>();
    }

    @Override
    void endDataRangeElement() {
        setDataRange(df.getOWLDatatypeRestriction(restrictedDataRange,
                facetRestrictions));
    }

    @Override
    void handleChild(@Nonnull AbstractOWLDataRangeHandler h) {
        OWLDataRange dr = h.getOWLObject();
        if (dr.isDatatype()) {
            restrictedDataRange = dr.asOWLDatatype();
        }
    }

    @Override
    void handleChild(@Nonnull OWLDatatypeFacetRestrictionElementHandler h) {
        facetRestrictions.add(h.getOWLObject());
    }
}

class OWLDeclarationAxiomElementHandler extends AbstractOWLAxiomElementHandler {

    @Nullable
    OWLEntity entity;
    // XXX this set seems unused
    @Nonnull
    Set<OWLAnnotation> entityAnnotations = new HashSet<OWLAnnotation>();

    OWLDeclarationAxiomElementHandler(@Nonnull OWLXMLParserHandler handler) {
        super(handler);
    }

    @Override
    void startElement(String name) {
        super.startElement(name);
        entity = null;
        entityAnnotations.clear();
    }

    @Override
    void handleChild(@Nonnull AbstractClassExpressionElementHandler h) {
        entity = (OWLClass) h.getOWLObject();
    }

    @Override
    void handleChild(@Nonnull AbstractOWLObjectPropertyElementHandler h) {
        entity = (OWLEntity) h.getOWLObject();
    }

    @Override
    void handleChild(@Nonnull OWLDataPropertyElementHandler h) {
        entity = (OWLEntity) h.getOWLObject();
    }

    @Override
    void handleChild(@Nonnull AbstractOWLDataRangeHandler h) {
        entity = (OWLEntity) h.getOWLObject();
    }

    @Override
    void handleChild(@Nonnull OWLAnnotationPropertyElementHandler h) {
        entity = h.getOWLObject();
    }

    @Override
    void handleChild(@Nonnull OWLIndividualElementHandler h) {
        entity = h.getOWLObject();
    }

    @Override
    OWLAxiom createAxiom() {
        return df.getOWLDeclarationAxiom(entity, getAnnotations());
    }

    @Override
    void handleChild(@Nonnull OWLAnnotationElementHandler h) {
        if (entity == null) {
            super.handleChild(h);
        } else {
            entityAnnotations.add(h.getOWLObject());
        }
    }
}

class OWLDifferentIndividualsAxiomElementHandler extends
        AbstractOWLIndividualOperandAxiomElementHandler {

    OWLDifferentIndividualsAxiomElementHandler(
            @Nonnull OWLXMLParserHandler handler) {
        super(handler);
    }

    @Override
    OWLAxiom createAxiom() {
        return df.getOWLDifferentIndividualsAxiom(getOperands(),
                getAnnotations());
    }
}

class OWLDisjointClassesAxiomElementHandler extends
        AbstractClassExpressionOperandAxiomElementHandler {

    OWLDisjointClassesAxiomElementHandler(@Nonnull OWLXMLParserHandler handler) {
        super(handler);
    }

    @Override
    OWLAxiom createAxiom() {
        return df.getOWLDisjointClassesAxiom(getOperands(), getAnnotations());
    }
}

class OWLDisjointDataPropertiesAxiomElementHandler extends
        AbstractOWLDataPropertyOperandAxiomElementHandler {

    OWLDisjointDataPropertiesAxiomElementHandler(
            @Nonnull OWLXMLParserHandler handler) {
        super(handler);
    }

    @Override
    OWLAxiom createAxiom() {
        return df.getOWLDisjointDataPropertiesAxiom(getOperands(),
                getAnnotations());
    }
}

class OWLDisjointObjectPropertiesAxiomElementHandler extends
        AbstractOWLObjectPropertyOperandAxiomElementHandler {

    OWLDisjointObjectPropertiesAxiomElementHandler(
            @Nonnull OWLXMLParserHandler handler) {
        super(handler);
    }

    @Override
    OWLAxiom createAxiom() {
        return df.getOWLDisjointObjectPropertiesAxiom(getOperands(),
                getAnnotations());
    }
}

class OWLDisjointUnionElementHandler extends AbstractOWLAxiomElementHandler {

    OWLClass cls;
    @Nonnull
    Set<OWLClassExpression> classExpressions = new HashSet<OWLClassExpression>();

    OWLDisjointUnionElementHandler(@Nonnull OWLXMLParserHandler handler) {
        super(handler);
    }

    @Override
    OWLAxiom createAxiom() {
        return df.getOWLDisjointUnionAxiom(cls, classExpressions,
                getAnnotations());
    }

    @Override
    void handleChild(@Nonnull AbstractClassExpressionElementHandler h) {
        if (cls == null) {
            OWLClassExpression desc = h.getOWLObject();
            cls = (OWLClass) desc;
        } else {
            classExpressions.add(h.getOWLObject());
        }
    }
}

class OWLEquivalentClassesAxiomElementHandler extends
        AbstractClassExpressionOperandAxiomElementHandler {

    OWLEquivalentClassesAxiomElementHandler(@Nonnull OWLXMLParserHandler handler) {
        super(handler);
    }

    @Override
    OWLAxiom createAxiom() {
        return df.getOWLEquivalentClassesAxiom(getOperands(), getAnnotations());
    }
}

class OWLEquivalentDataPropertiesAxiomElementHandler extends
        AbstractOWLDataPropertyOperandAxiomElementHandler {

    OWLEquivalentDataPropertiesAxiomElementHandler(
            @Nonnull OWLXMLParserHandler handler) {
        super(handler);
    }

    @Override
    OWLAxiom createAxiom() {
        return df.getOWLEquivalentDataPropertiesAxiom(getOperands(),
                getAnnotations());
    }
}

class OWLEquivalentObjectPropertiesAxiomElementHandler extends
        AbstractOWLObjectPropertyOperandAxiomElementHandler {

    OWLEquivalentObjectPropertiesAxiomElementHandler(
            @Nonnull OWLXMLParserHandler handler) {
        super(handler);
    }

    @Override
    OWLAxiom createAxiom() {
        return df.getOWLEquivalentObjectPropertiesAxiom(getOperands(),
                getAnnotations());
    }
}

class OWLFunctionalDataPropertyAxiomElementHandler
        extends
        AbstractOWLPropertyCharacteristicAxiomElementHandler<OWLDataPropertyExpression> {

    OWLFunctionalDataPropertyAxiomElementHandler(
            @Nonnull OWLXMLParserHandler handler) {
        super(handler);
    }

    @Override
    void handleChild(@Nonnull OWLDataPropertyElementHandler h) {
        setProperty(h.getOWLObject());
    }

    @Override
    OWLAxiom createPropertyCharacteristicAxiom() {
        ensureNotNull(getProperty(), "Expected data property element");
        return df.getOWLFunctionalDataPropertyAxiom(getProperty(),
                getAnnotations());
    }
}

class OWLFunctionalObjectPropertyAxiomElementHandler extends
        AbstractOWLObjectPropertyCharacteristicAxiomElementHandler {

    OWLFunctionalObjectPropertyAxiomElementHandler(
            @Nonnull OWLXMLParserHandler handler) {
        super(handler);
    }

    @Override
    OWLAxiom createPropertyCharacteristicAxiom() {
        return df.getOWLFunctionalObjectPropertyAxiom(getProperty(),
                getAnnotations());
    }
}

class OWLHasKeyElementHandler extends AbstractOWLAxiomElementHandler {

    OWLClassExpression ce;
    @Nonnull
    Set<OWLPropertyExpression> props = new HashSet<OWLPropertyExpression>();

    OWLHasKeyElementHandler(@Nonnull OWLXMLParserHandler handler) {
        super(handler);
    }

    @Override
    void startElement(String name) {
        super.startElement(name);
        props.clear();
    }

    @Override
    void handleChild(@Nonnull AbstractClassExpressionElementHandler h) {
        ce = h.getOWLObject();
    }

    @Override
    void handleChild(@Nonnull OWLDataPropertyElementHandler h) {
        props.add(h.getOWLObject());
    }

    @Override
    void handleChild(@Nonnull AbstractOWLObjectPropertyElementHandler h) {
        props.add(h.getOWLObject());
    }

    @Override
    OWLAxiom createAxiom() {
        return df.getOWLHasKeyAxiom(ce, props, getAnnotations());
    }
}

class OWLIndividualElementHandler extends OWLElementHandler<OWLNamedIndividual> {

    OWLNamedIndividual individual;
    @Nullable
    IRI name;

    OWLIndividualElementHandler(@Nonnull OWLXMLParserHandler handler) {
        super(handler);
    }

    @Override
    OWLNamedIndividual getOWLObject() {
        return individual;
    }

    @Override
    void attribute(@Nonnull String localName, @Nonnull String value) {
        name = getIRIFromAttribute(localName, value);
    }

    @Override
    void endElement() {
        // URI uri = getNameAttribute();
        individual = df.getOWLNamedIndividual(name);
        getParentHandler().handleChild(this);
    }
}

class OWLInverseFunctionalObjectPropertyAxiomElementHandler extends
        AbstractOWLObjectPropertyCharacteristicAxiomElementHandler {

    OWLInverseFunctionalObjectPropertyAxiomElementHandler(
            @Nonnull OWLXMLParserHandler handler) {
        super(handler);
    }

    @Override
    OWLAxiom createPropertyCharacteristicAxiom() {
        return df.getOWLInverseFunctionalObjectPropertyAxiom(getProperty(),
                getAnnotations());
    }
}

class OWLInverseObjectPropertiesAxiomElementHandler extends
        AbstractOWLObjectPropertyOperandAxiomElementHandler {

    OWLInverseObjectPropertiesAxiomElementHandler(
            @Nonnull OWLXMLParserHandler handler) {
        super(handler);
    }

    @Override
    OWLAxiom createAxiom() {
        Set<OWLObjectPropertyExpression> props = getOperands();
        if (props.size() > 2 || props.size() < 1) {
            ensureNotNull(null,
                    "Expected 2 object property expression elements");
        }
        Iterator<OWLObjectPropertyExpression> it = props.iterator();
        OWLObjectPropertyExpression propA = it.next();
        OWLObjectPropertyExpression propB;
        if (it.hasNext()) {
            propB = it.next();
        } else {
            // Syntactic variant of symmetric property
            propB = propA;
        }
        return df.getOWLInverseObjectPropertiesAxiom(propA, propB,
                getAnnotations());
    }
}

class OWLInverseObjectPropertyElementHandler extends
        AbstractOWLObjectPropertyElementHandler {

    OWLObjectPropertyExpression inverse;

    OWLInverseObjectPropertyElementHandler(@Nonnull OWLXMLParserHandler handler) {
        super(handler);
    }

    @Override
    void handleChild(@Nonnull AbstractOWLObjectPropertyElementHandler h) {
        inverse = h.getOWLObject();
    }

    @Override
    void endObjectPropertyElement() {
        ensureNotNull(inverse, OBJECT_INVERSE_OF.getShortForm());
        setOWLObjectPropertyExpression(df.getOWLObjectInverseOf(inverse));
    }
}

class OWLIrreflexiveObjectPropertyAxiomElementHandler extends
        AbstractOWLObjectPropertyCharacteristicAxiomElementHandler {

    OWLIrreflexiveObjectPropertyAxiomElementHandler(
            @Nonnull OWLXMLParserHandler handler) {
        super(handler);
    }

    @Override
    OWLAxiom createPropertyCharacteristicAxiom() {
        return df.getOWLIrreflexiveObjectPropertyAxiom(getProperty(),
                getAnnotations());
    }
}

class OWLLiteralElementHandler extends OWLElementHandler<OWLLiteral> {

    OWLLiteral literal;
    @Nullable
    IRI iri;
    @Nullable
    String lang;

    OWLLiteralElementHandler(@Nonnull OWLXMLParserHandler handler) {
        super(handler);
    }

    @Override
    void startElement(String name) {
        super.startElement(name);
    }

    @Override
    void attribute(@Nonnull String localName, @Nonnull String value) {
        if (localName.equals(DATATYPE_IRI.getShortForm())) {
            iri = handler.getIRI(value);
        } else if (localName.equals("lang")) {
            lang = value;
        }
    }

    @Override
    void endElement() {
        if (iri != null && !iri.isPlainLiteral()) {
            literal = df.getOWLLiteral(getText(), df.getOWLDatatype(iri));
        } else {
            literal = df.getOWLLiteral(getText(), lang);
        }
        lang = null;
        iri = null;
        getParentHandler().handleChild(this);
    }

    @Override
    OWLLiteral getOWLObject() {
        return literal;
    }

    @Override
    boolean isTextContentPossible() {
        return true;
    }
}

class OWLNegativeDataPropertyAssertionAxiomElementHandler extends
        AbstractOWLDataPropertyAssertionAxiomElementHandler {

    OWLNegativeDataPropertyAssertionAxiomElementHandler(
            @Nonnull OWLXMLParserHandler handler) {
        super(handler);
    }

    @Override
    OWLAxiom createAxiom() {
        return df.getOWLNegativeDataPropertyAssertionAxiom(getProperty(),
                getSubject(), getObject(), getAnnotations());
    }
}

class OWLNegativeObjectPropertyAssertionAxiomElementHandler extends
        AbstractOWLObjectPropertyAssertionAxiomElementHandler {

    OWLNegativeObjectPropertyAssertionAxiomElementHandler(
            @Nonnull OWLXMLParserHandler handler) {
        super(handler);
    }

    @Override
    OWLAxiom createAxiom() {
        return df.getOWLNegativeObjectPropertyAssertionAxiom(getProperty(),
                getSubject(), getObject(), getAnnotations());
    }
}

class OWLObjectAllValuesFromElementHandler extends
        AbstractClassExpressionFillerRestriction {

    OWLObjectAllValuesFromElementHandler(@Nonnull OWLXMLParserHandler handler) {
        super(handler);
    }

    @Override
    OWLClassExpression createRestriction() {
        return df.getOWLObjectAllValuesFrom(getProperty(), getFiller());
    }
}

class OWLObjectComplementOfElementHandler extends
        AbstractClassExpressionElementHandler {

    OWLClassExpression operand;

    OWLObjectComplementOfElementHandler(@Nonnull OWLXMLParserHandler handler) {
        super(handler);
    }

    @Override
    void handleChild(@Nonnull AbstractClassExpressionElementHandler h) {
        operand = h.getOWLObject();
    }

    @Override
    void endClassExpressionElement() {
        ensureNotNull(operand, "class expression element");
        setClassExpression(df.getOWLObjectComplementOf(operand));
    }
}

class OWLObjectExactCardinalityElementHandler extends
        AbstractOWLObjectCardinalityElementHandler {

    OWLObjectExactCardinalityElementHandler(@Nonnull OWLXMLParserHandler handler) {
        super(handler);
    }

    @Override
    OWLClassExpression createCardinalityRestriction() {
        return df.getOWLObjectExactCardinality(getCardinality(), getProperty(),
                getFiller());
    }
}

class OWLObjectExistsSelfElementHandler extends
        AbstractClassExpressionElementHandler {

    OWLObjectPropertyExpression property;

    OWLObjectExistsSelfElementHandler(@Nonnull OWLXMLParserHandler handler) {
        super(handler);
    }

    @Override
    void handleChild(@Nonnull AbstractOWLObjectPropertyElementHandler h) {
        property = h.getOWLObject();
    }

    @Override
    void endClassExpressionElement() {
        ensureNotNull(property,
                "Was expecting object property expression element");
        setClassExpression(df.getOWLObjectHasSelf(property));
    }
}

class OWLObjectHasValueElementHandler extends
        AbstractObjectRestrictionElementHandler<OWLIndividual> {

    OWLObjectHasValueElementHandler(@Nonnull OWLXMLParserHandler handler) {
        super(handler);
    }

    @Override
    void handleChild(@Nonnull OWLIndividualElementHandler h) {
        setFiller(h.getOWLObject());
    }

    @Override
    void handleChild(@Nonnull OWLAnonymousIndividualElementHandler h) {
        setFiller(h.getOWLObject());
    }

    @Override
    OWLClassExpression createRestriction() {
        return df.getOWLObjectHasValue(getProperty(), getFiller());
    }
}

class OWLObjectIntersectionOfElementHandler extends
        AbstractNaryBooleanClassExpressionElementHandler {

    OWLObjectIntersectionOfElementHandler(@Nonnull OWLXMLParserHandler handler) {
        super(handler);
    }

    @Override
    OWLClassExpression createClassExpression(Set<OWLClassExpression> ops) {
        return df.getOWLObjectIntersectionOf(ops);
    }
}

class OWLObjectMaxCardinalityElementHandler extends
        AbstractOWLObjectCardinalityElementHandler {

    OWLObjectMaxCardinalityElementHandler(@Nonnull OWLXMLParserHandler handler) {
        super(handler);
    }

    @Override
    OWLClassExpression createCardinalityRestriction() {
        return df.getOWLObjectMaxCardinality(getCardinality(), getProperty(),
                getFiller());
    }
}

class OWLObjectMinCardinalityElementHandler extends
        AbstractOWLObjectCardinalityElementHandler {

    OWLObjectMinCardinalityElementHandler(@Nonnull OWLXMLParserHandler handler) {
        super(handler);
    }

    @Override
    OWLClassExpression createCardinalityRestriction() {
        return df.getOWLObjectMinCardinality(getCardinality(), getProperty(),
                getFiller());
    }
}

class OWLObjectOneOfElementHandler extends
        AbstractClassExpressionElementHandler {

    @Nonnull
    Set<OWLIndividual> individuals = new HashSet<OWLIndividual>();

    OWLObjectOneOfElementHandler(@Nonnull OWLXMLParserHandler handler) {
        super(handler);
    }

    @Override
    void handleChild(@Nonnull OWLIndividualElementHandler h) {
        individuals.add(h.getOWLObject());
    }

    @Override
    void endClassExpressionElement() {
        if (individuals.size() < 1) {
            ensureNotNull(null,
                    "Expected at least one individual in object oneOf");
        }
        setClassExpression(df.getOWLObjectOneOf(individuals));
    }
}

class OWLObjectPropertyAssertionAxiomElementHandler extends
        AbstractOWLObjectPropertyAssertionAxiomElementHandler {

    OWLObjectPropertyAssertionAxiomElementHandler(
            @Nonnull OWLXMLParserHandler handler) {
        super(handler);
    }

    @Override
    OWLAxiom createAxiom() {
        return df.getOWLObjectPropertyAssertionAxiom(getProperty(),
                getSubject(), getObject(), getAnnotations());
    }
}

class OWLObjectPropertyDomainElementHandler extends
        AbstractOWLAxiomElementHandler {

    OWLClassExpression domain;
    OWLObjectPropertyExpression property;

    OWLObjectPropertyDomainElementHandler(@Nonnull OWLXMLParserHandler handler) {
        super(handler);
    }

    @Override
    void handleChild(@Nonnull AbstractClassExpressionElementHandler h) {
        domain = h.getOWLObject();
    }

    @Override
    void handleChild(@Nonnull AbstractOWLObjectPropertyElementHandler h) {
        property = h.getOWLObject();
    }

    @Override
    OWLAxiom createAxiom() {
        ensureNotNull(property, "Expected object property element");
        ensureNotNull(domain, "Expected class expression element");
        return df.getOWLObjectPropertyDomainAxiom(property, domain,
                getAnnotations());
    }
}

class OWLObjectPropertyElementHandler extends
        AbstractOWLObjectPropertyElementHandler {

    @Nullable
    IRI iri;

    OWLObjectPropertyElementHandler(@Nonnull OWLXMLParserHandler handler) {
        super(handler);
    }

    @Override
    void attribute(@Nonnull String localName, @Nonnull String value) {
        iri = getIRIFromAttribute(localName, value);
    }

    @Override
    void endObjectPropertyElement() {
        setOWLObjectPropertyExpression(df.getOWLObjectProperty(iri));
    }
}

class OWLObjectPropertyRangeAxiomElementHandler extends
        AbstractOWLAxiomElementHandler {

    OWLClassExpression range;
    OWLObjectPropertyExpression property;

    OWLObjectPropertyRangeAxiomElementHandler(
            @Nonnull OWLXMLParserHandler handler) {
        super(handler);
    }

    @Override
    void handleChild(@Nonnull AbstractClassExpressionElementHandler h) {
        range = h.getOWLObject();
    }

    @Override
    void handleChild(@Nonnull AbstractOWLObjectPropertyElementHandler h) {
        property = h.getOWLObject();
    }

    @Override
    OWLAxiom createAxiom() {
        ensureNotNull(property, OBJECT_PROPERTY.getShortForm());
        ensureNotNull(range, "OWL class expression element");
        return df.getOWLObjectPropertyRangeAxiom(property, range,
                getAnnotations());
    }
}

class OWLObjectSomeValuesFromElementHandler extends
        AbstractClassExpressionFillerRestriction {

    OWLObjectSomeValuesFromElementHandler(@Nonnull OWLXMLParserHandler handler) {
        super(handler);
    }

    @Override
    OWLClassExpression createRestriction() {
        return df.getOWLObjectSomeValuesFrom(getProperty(), getFiller());
    }
}

class OWLObjectUnionOfElementHandler extends
        AbstractNaryBooleanClassExpressionElementHandler {

    OWLObjectUnionOfElementHandler(@Nonnull OWLXMLParserHandler handler) {
        super(handler);
    }

    @Override
    OWLClassExpression createClassExpression(Set<OWLClassExpression> ops) {
        return df.getOWLObjectUnionOf(ops);
    }
}

class OWLReflexiveObjectPropertyAxiomElementHandler extends
        AbstractOWLObjectPropertyCharacteristicAxiomElementHandler {

    OWLReflexiveObjectPropertyAxiomElementHandler(
            @Nonnull OWLXMLParserHandler handler) {
        super(handler);
    }

    @Override
    OWLAxiom createPropertyCharacteristicAxiom() {
        return df.getOWLReflexiveObjectPropertyAxiom(getProperty(),
                getAnnotations());
    }
}

class OWLSameIndividualsAxiomElementHandler extends
        AbstractOWLIndividualOperandAxiomElementHandler {

    OWLSameIndividualsAxiomElementHandler(@Nonnull OWLXMLParserHandler handler) {
        super(handler);
    }

    @Override
    OWLAxiom createAxiom() {
        return df.getOWLSameIndividualAxiom(getOperands(), getAnnotations());
    }
}

class OWLSubAnnotationPropertyOfElementHandler extends
        AbstractOWLAxiomElementHandler {

    @Nullable
    OWLAnnotationProperty subProperty = null;
    @Nullable
    OWLAnnotationProperty superProperty = null;

    OWLSubAnnotationPropertyOfElementHandler(
            @Nonnull OWLXMLParserHandler handler) {
        super(handler);
    }

    @Override
    void handleChild(@Nonnull OWLAnnotationPropertyElementHandler h) {
        if (subProperty == null) {
            subProperty = h.getOWLObject();
        } else if (superProperty == null) {
            superProperty = h.getOWLObject();
        } else {
            ensureNotNull(null, "two annotation properties elements");
        }
    }

    @Override
    OWLAxiom createAxiom() {
        ensureNotNull(subProperty, "AnnotationProperty for sub property");
        ensureNotNull(superProperty, "AnnotationProperty for super property");
        return df.getOWLSubAnnotationPropertyOfAxiom(subProperty,
                superProperty, getAnnotations());
    }
}

class OWLSubClassAxiomElementHandler extends AbstractOWLAxiomElementHandler {

    @Nullable
    OWLClassExpression subClass;
    @Nullable
    OWLClassExpression supClass;

    OWLSubClassAxiomElementHandler(@Nonnull OWLXMLParserHandler handler) {
        super(handler);
    }

    @Override
    void startElement(String name) {
        super.startElement(name);
        subClass = null;
        supClass = null;
    }

    @Override
    void handleChild(@Nonnull AbstractClassExpressionElementHandler h) {
        if (subClass == null) {
            subClass = h.getOWLObject();
        } else if (supClass == null) {
            supClass = h.getOWLObject();
        }
    }

    @Override
    OWLAxiom createAxiom() {
        return df.getOWLSubClassOfAxiom(subClass, supClass, getAnnotations());
    }
}

class OWLSubDataPropertyOfAxiomElementHandler extends
        AbstractOWLAxiomElementHandler {

    OWLDataPropertyExpression subProperty;
    OWLDataPropertyExpression superProperty;

    OWLSubDataPropertyOfAxiomElementHandler(@Nonnull OWLXMLParserHandler handler) {
        super(handler);
    }

    @Override
    void handleChild(@Nonnull OWLDataPropertyElementHandler h) {
        if (subProperty == null) {
            subProperty = h.getOWLObject();
        } else if (superProperty == null) {
            superProperty = h.getOWLObject();
        } else {
            ensureNotNull(null, "two data property expression elements");
        }
    }

    @Override
    OWLAxiom createAxiom() {
        return df.getOWLSubDataPropertyOfAxiom(subProperty, superProperty,
                getAnnotations());
    }
}

class OWLSubObjectPropertyChainElementHandler extends
        OWLElementHandler<List<OWLObjectPropertyExpression>> {

    List<OWLObjectPropertyExpression> propertyList;

    OWLSubObjectPropertyChainElementHandler(@Nonnull OWLXMLParserHandler handler) {
        super(handler);
        propertyList = new ArrayList<OWLObjectPropertyExpression>();
    }

    @Override
    void endElement() {
        getParentHandler().handleChild(this);
    }

    @Override
    List<OWLObjectPropertyExpression> getOWLObject() {
        return propertyList;
    }

    @Override
    void handleChild(@Nonnull AbstractOWLObjectPropertyElementHandler h) {
        propertyList.add(h.getOWLObject());
    }
}

class OWLSubObjectPropertyOfAxiomElementHandler extends
        AbstractOWLAxiomElementHandler {

    OWLObjectPropertyExpression subProperty;
    List<OWLObjectPropertyExpression> propertyList;
    OWLObjectPropertyExpression superProperty;

    OWLSubObjectPropertyOfAxiomElementHandler(
            @Nonnull OWLXMLParserHandler handler) {
        super(handler);
    }

    @Override
    void handleChild(@Nonnull AbstractOWLObjectPropertyElementHandler h) {
        if (subProperty == null && propertyList == null) {
            subProperty = h.getOWLObject();
        } else if (superProperty == null) {
            superProperty = h.getOWLObject();
        } else {
            ensureNotNull(null,
                    "Expected two object property expression elements");
        }
    }

    @Override
    void handleChild(@Nonnull OWLSubObjectPropertyChainElementHandler h) {
        propertyList = h.getOWLObject();
    }

    @Override
    OWLAxiom createAxiom() {
        if (subProperty != null) {
            return df.getOWLSubObjectPropertyOfAxiom(subProperty,
                    superProperty, getAnnotations());
        } else {
            return df.getOWLSubPropertyChainOfAxiom(propertyList,
                    superProperty, getAnnotations());
        }
    }
}

class OWLSymmetricObjectPropertyAxiomElementHandler extends
        AbstractOWLObjectPropertyCharacteristicAxiomElementHandler {

    OWLSymmetricObjectPropertyAxiomElementHandler(
            @Nonnull OWLXMLParserHandler handler) {
        super(handler);
    }

    @Override
    OWLAxiom createPropertyCharacteristicAxiom() {
        return df.getOWLSymmetricObjectPropertyAxiom(getProperty(),
                getAnnotations());
    }
}

class OWLTransitiveObjectPropertyAxiomElementHandler extends
        AbstractOWLObjectPropertyCharacteristicAxiomElementHandler {

    OWLTransitiveObjectPropertyAxiomElementHandler(
            @Nonnull OWLXMLParserHandler handler) {
        super(handler);
    }

    @Override
    OWLAxiom createPropertyCharacteristicAxiom() {
        return df.getOWLTransitiveObjectPropertyAxiom(getProperty(),
                getAnnotations());
    }
}

abstract class SWRLAtomElementHandler extends OWLElementHandler<SWRLAtom> {

    SWRLAtom atom;

    SWRLAtomElementHandler(@Nonnull OWLXMLParserHandler handler) {
        super(handler);
    }

    void setAtom(SWRLAtom atom) {
        this.atom = atom;
    }

    @Override
    SWRLAtom getOWLObject() {
        return atom;
    }
}

class SWRLAtomListElementHandler extends OWLElementHandler<List<SWRLAtom>> {

    @Nonnull
    List<SWRLAtom> atoms = new ArrayList<SWRLAtom>();

    SWRLAtomListElementHandler(@Nonnull OWLXMLParserHandler handler) {
        super(handler);
    }

    @Override
    void handleChild(@Nonnull SWRLAtomElementHandler h) {
        atoms.add(h.getOWLObject());
    }

    @Nonnull
    @Override
    List<SWRLAtom> getOWLObject() {
        return atoms;
    }

    @Override
    void endElement() {
        getParentHandler().handleChild(this);
    }
}

class SWRLBuiltInAtomElementHandler extends SWRLAtomElementHandler {

    @Nullable
    IRI iri;
    @Nonnull
    List<SWRLDArgument> args = new ArrayList<SWRLDArgument>();

    SWRLBuiltInAtomElementHandler(@Nonnull OWLXMLParserHandler handler) {
        super(handler);
    }

    @Override
    void attribute(@Nonnull String localName, @Nonnull String value) {
        iri = getIRIFromAttribute(localName, value);
    }

    @Override
    void handleChild(@Nonnull SWRLVariableElementHandler h) {
        args.add(h.getOWLObject());
    }

    @Override
    void handleChild(@Nonnull OWLLiteralElementHandler h) {
        args.add(df.getSWRLLiteralArgument(h.getOWLObject()));
    }

    @Override
    void endElement() {
        setAtom(df.getSWRLBuiltInAtom(iri, args));
        getParentHandler().handleChild(this);
    }
}

class SWRLClassAtomElementHandler extends SWRLAtomElementHandler {

    OWLClassExpression ce;
    @Nullable
    SWRLIArgument arg;

    SWRLClassAtomElementHandler(@Nonnull OWLXMLParserHandler handler) {
        super(handler);
    }

    @Override
    void handleChild(@Nonnull SWRLVariableElementHandler h) {
        arg = h.getOWLObject();
    }

    @Override
    void handleChild(@Nonnull AbstractClassExpressionElementHandler h) {
        ce = h.getOWLObject();
    }

    @Override
    void handleChild(@Nonnull OWLIndividualElementHandler h) {
        arg = df.getSWRLIndividualArgument(h.getOWLObject());
    }

    @Override
    void endElement() {
        setAtom(df.getSWRLClassAtom(ce, arg));
        getParentHandler().handleChild(this);
    }
}

class SWRLDataPropertyAtomElementHandler extends SWRLAtomElementHandler {

    OWLDataPropertyExpression prop;
    @Nullable
    SWRLIArgument arg0 = null;
    @Nullable
    SWRLDArgument arg1 = null;

    SWRLDataPropertyAtomElementHandler(@Nonnull OWLXMLParserHandler handler) {
        super(handler);
    }

    @Override
    void handleChild(@Nonnull OWLDataPropertyElementHandler h) {
        prop = h.getOWLObject();
    }

    @Override
    void handleChild(@Nonnull SWRLVariableElementHandler h) {
        if (arg0 == null) {
            arg0 = h.getOWLObject();
        } else if (arg1 == null) {
            arg1 = h.getOWLObject();
        }
    }

    @Override
    void handleChild(@Nonnull OWLLiteralElementHandler h) {
        arg1 = df.getSWRLLiteralArgument(h.getOWLObject());
    }

    @Override
    void handleChild(@Nonnull OWLIndividualElementHandler _handler) {
        arg0 = df.getSWRLIndividualArgument(_handler.getOWLObject());
    }

    @Override
    void handleChild(@Nonnull OWLAnonymousIndividualElementHandler _handler) {
        arg0 = df.getSWRLIndividualArgument(_handler.getOWLObject());
    }

    @Override
    void endElement() {
        setAtom(df.getSWRLDataPropertyAtom(prop, arg0, arg1));
        getParentHandler().handleChild(this);
    }
}

class SWRLDataRangeAtomElementHandler extends SWRLAtomElementHandler {

    OWLDataRange prop;
    @Nullable
    SWRLDArgument arg1 = null;

    SWRLDataRangeAtomElementHandler(@Nonnull OWLXMLParserHandler handler) {
        super(handler);
    }

    @Override
    void handleChild(@Nonnull AbstractOWLDataRangeHandler _handler) {
        prop = _handler.getOWLObject();
    }

    @Override
    void handleChild(@Nonnull SWRLVariableElementHandler h) {
        arg1 = h.getOWLObject();
    }

    @Override
    void handleChild(@Nonnull OWLLiteralElementHandler h) {
        arg1 = df.getSWRLLiteralArgument(h.getOWLObject());
    }

    @Override
    void endElement() {
        setAtom(df.getSWRLDataRangeAtom(prop, arg1));
        getParentHandler().handleChild(this);
    }
}

class SWRLDifferentIndividualsAtomElementHandler extends SWRLAtomElementHandler {

    @Nullable
    SWRLIArgument arg0;
    @Nullable
    SWRLIArgument arg1;

    SWRLDifferentIndividualsAtomElementHandler(
            @Nonnull OWLXMLParserHandler handler) {
        super(handler);
    }

    @Override
    void handleChild(@Nonnull SWRLVariableElementHandler h) {
        if (arg0 == null) {
            arg0 = h.getOWLObject();
        } else if (arg1 == null) {
            arg1 = h.getOWLObject();
        }
    }

    @Override
    void handleChild(@Nonnull OWLIndividualElementHandler h) {
        if (arg0 == null) {
            arg0 = df.getSWRLIndividualArgument(h.getOWLObject());
        } else if (arg1 == null) {
            arg1 = df.getSWRLIndividualArgument(h.getOWLObject());
        }
    }

    @Override
    void endElement() {
        setAtom(df.getSWRLDifferentIndividualsAtom(arg0, arg1));
        getParentHandler().handleChild(this);
    }
}

class SWRLObjectPropertyAtomElementHandler extends SWRLAtomElementHandler {

    OWLObjectPropertyExpression prop;
    @Nullable
    SWRLIArgument arg0 = null;
    @Nullable
    SWRLIArgument arg1 = null;

    SWRLObjectPropertyAtomElementHandler(@Nonnull OWLXMLParserHandler handler) {
        super(handler);
    }

    @Override
    void handleChild(@Nonnull AbstractOWLObjectPropertyElementHandler h) {
        prop = h.getOWLObject();
    }

    @Override
    void handleChild(@Nonnull SWRLVariableElementHandler h) {
        if (arg0 == null) {
            arg0 = h.getOWLObject();
        } else if (arg1 == null) {
            arg1 = h.getOWLObject();
        }
    }

    @Override
    void handleChild(@Nonnull OWLIndividualElementHandler h) {
        if (arg0 == null) {
            arg0 = df.getSWRLIndividualArgument(h.getOWLObject());
        } else if (arg1 == null) {
            arg1 = df.getSWRLIndividualArgument(h.getOWLObject());
        }
    }

    @Override
    void endElement() {
        setAtom(df.getSWRLObjectPropertyAtom(prop, arg0, arg1));
        getParentHandler().handleChild(this);
    }
}

class SWRLRuleElementHandler extends AbstractOWLAxiomElementHandler {

    @Nullable
    Set<SWRLAtom> body = null;
    @Nullable
    Set<SWRLAtom> head = null;

    SWRLRuleElementHandler(@Nonnull OWLXMLParserHandler handler) {
        super(handler);
    }

    @Override
    OWLAxiom createAxiom() {
        return df.getSWRLRule(body, head, getAnnotations());
    }

    @Override
    void handleChild(@Nonnull SWRLAtomListElementHandler h) {
        if (body == null) {
            body = new LinkedHashSet<SWRLAtom>(h.getOWLObject());
        } else if (head == null) {
            head = new LinkedHashSet<SWRLAtom>(h.getOWLObject());
        }
    }
}

class SWRLSameIndividualAtomElementHandler extends SWRLAtomElementHandler {

    @Nullable
    SWRLIArgument arg0;
    @Nullable
    SWRLIArgument arg1;

    SWRLSameIndividualAtomElementHandler(@Nonnull OWLXMLParserHandler handler) {
        super(handler);
    }

    @Override
    void handleChild(@Nonnull SWRLVariableElementHandler h) {
        if (arg0 == null) {
            arg0 = h.getOWLObject();
        } else if (arg1 == null) {
            arg1 = h.getOWLObject();
        }
    }

    @Override
    void handleChild(@Nonnull OWLIndividualElementHandler h) {
        if (arg0 == null) {
            arg0 = df.getSWRLIndividualArgument(h.getOWLObject());
        } else if (arg1 == null) {
            arg1 = df.getSWRLIndividualArgument(h.getOWLObject());
        }
    }

    @Override
    void endElement() {
        setAtom(df.getSWRLSameIndividualAtom(arg0, arg1));
        getParentHandler().handleChild(this);
    }
}

class SWRLVariableElementHandler extends OWLElementHandler<SWRLVariable> {

    SWRLVariableElementHandler(@Nonnull OWLXMLParserHandler handler) {
        super(handler);
    }

    @Nullable
    IRI iri;

    @Override
    void endElement() {
        getParentHandler().handleChild(this);
    }

    @Override
    void attribute(@Nonnull String localName, @Nonnull String value) {
        iri = getIRIFromAttribute(localName, value);
    }

    @Nullable
    @Override
    SWRLVariable getOWLObject() {
        if (iri != null) {
            return df.getSWRLVariable(iri);
        }
        return null;
    }
}

class OWLOntologyHandler extends OWLElementHandler<OWLOntology> {

    OWLOntologyHandler(@Nonnull OWLXMLParserHandler handler) {
        super(handler);
    }

    @Override
    void startElement(String name) {}

    @Override
    void attribute(@Nonnull String name, String value) {
        if (name.equals("ontologyIRI")) {
            OWLOntologyID newID = new OWLOntologyID(Optional.of(IRI
                    .create(value)), handler.getOntology().getOntologyID()
                    .getVersionIRI());
            handler.getOWLOntologyManager().applyChange(
                    new SetOntologyID(handler.getOntology(), newID));
        }
        if (name.equals("versionIRI")) {
            OWLOntologyID newID = new OWLOntologyID(handler.getOntology()
                    .getOntologyID().getOntologyIRI(), Optional.of(IRI
                    .create(value)));
            handler.getOWLOntologyManager().applyChange(
                    new SetOntologyID(handler.getOntology(), newID));
        }
    }

    @Override
    void handleChild(@Nonnull AbstractOWLAxiomElementHandler h) {
        OWLAxiom axiom = h.getOWLObject();
        if (!axiom.isAnnotationAxiom()
                || handler.getConfiguration().isLoadAnnotationAxioms()) {
            handler.getOWLOntologyManager().applyChange(
                    new AddAxiom(handler.getOntology(), axiom));
        }
    }

    @Override
    void handleChild(AbstractOWLDataRangeHandler h) {}

    @Override
    void handleChild(AbstractClassExpressionElementHandler h) {}

    @Override
    void handleChild(@Nonnull OWLAnnotationElementHandler h) {
        handler.getOWLOntologyManager().applyChange(
                new AddOntologyAnnotation(handler.getOntology(), h
                        .getOWLObject()));
    }

    @Override
    void endElement() {}

    @Override
    OWLOntology getOWLObject() {
        return handler.getOntology();
    }

    @Override
    void setParentHandler(OWLElementHandler<?> handler) {}
}

class OWLImportsHandler extends OWLElementHandler<OWLOntology> {

    OWLImportsHandler(@Nonnull OWLXMLParserHandler handler) {
        super(handler);
    }

    @Override
    void endElement() {
        IRI ontIRI = handler.getIRI(getText().trim());
        OWLImportsDeclaration decl = df.getOWLImportsDeclaration(ontIRI);
        handler.getOWLOntologyManager().applyChange(
                new AddImport(handler.getOntology(), decl));
        handler.getOWLOntologyManager().makeLoadImportRequest(decl,
                handler.getConfiguration());
    }

    @Nullable
    @Override
    OWLOntology getOWLObject() {
        return null;
    }

    @Override
    boolean isTextContentPossible() {
        return true;
    }
}