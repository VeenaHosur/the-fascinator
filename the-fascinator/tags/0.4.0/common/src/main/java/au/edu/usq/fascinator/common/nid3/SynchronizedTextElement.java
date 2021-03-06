/**
 * generated by http://RDFReactor.semweb4j.org ($Id: CodeGenerator.java 1535 2008-09-09 15:44:46Z max.at.xam.de $) on 15/10/09 2:48 PM
 */
package au.edu.usq.fascinator.common.nid3;

import org.ontoware.aifbcommons.collection.ClosableIterator;
import org.ontoware.rdf2go.exception.ModelRuntimeException;
import org.ontoware.rdf2go.model.Model;
import org.ontoware.rdf2go.model.node.BlankNode;
import org.ontoware.rdf2go.model.node.URI;
import org.ontoware.rdf2go.model.node.impl.URIImpl;
import org.ontoware.rdfreactor.runtime.Base;
import org.ontoware.rdfreactor.runtime.ReactorResult;

/**
 * This class manages access to these properties:
 * <ul>
 *   <li> TextElementContent </li>
 *   <li> TextElementTimestamp </li>
 * </ul>
 *
 * This class was generated by <a href="http://RDFReactor.semweb4j.org">RDFReactor</a> on 15/10/09 2:48 PM
 */
public class SynchronizedTextElement extends Thing {

    /** http://www.semanticdesktop.org/ontologies/2007/05/10/nid3#SynchronizedTextElement */
    @SuppressWarnings("hiding")
	public static final URI RDFS_CLASS = new URIImpl("http://www.semanticdesktop.org/ontologies/2007/05/10/nid3#SynchronizedTextElement", false);

    /** http://www.semanticdesktop.org/ontologies/2007/05/10/nid3#textElementContent */
    @SuppressWarnings("hiding")
	public static final URI TEXTELEMENTCONTENT = new URIImpl("http://www.semanticdesktop.org/ontologies/2007/05/10/nid3#textElementContent",false);

    /** http://www.semanticdesktop.org/ontologies/2007/05/10/nid3#textElementTimestamp */
    @SuppressWarnings("hiding")
	public static final URI TEXTELEMENTTIMESTAMP = new URIImpl("http://www.semanticdesktop.org/ontologies/2007/05/10/nid3#textElementTimestamp",false);

    /** 
     * All property-URIs with this class as domain.
     * All properties of all super-classes are also available. 
     */
    @SuppressWarnings("hiding")
    public static final URI[] MANAGED_URIS = {
      new URIImpl("http://www.semanticdesktop.org/ontologies/2007/05/10/nid3#textElementContent",false),
      new URIImpl("http://www.semanticdesktop.org/ontologies/2007/05/10/nid3#textElementTimestamp",false) 
    };


	// protected constructors needed for inheritance
	
	/**
	 * Returns a Java wrapper over an RDF object, identified by URI.
	 * Creating two wrappers for the same instanceURI is legal.
	 * @param model RDF2GO Model implementation, see http://rdf2go.semweb4j.org
	 * @param classURI URI of RDFS class
	 * @param instanceIdentifier Resource that identifies this instance
	 * @param write if true, the statement (this, rdf:type, TYPE) is written to the model
	 *
	 * [Generated from RDFReactor template rule #c1] 
	 */
	protected SynchronizedTextElement ( Model model, URI classURI, org.ontoware.rdf2go.model.node.Resource instanceIdentifier, boolean write ) {
		super(model, classURI, instanceIdentifier, write);
	}

	// public constructors

	/**
	 * Returns a Java wrapper over an RDF object, identified by URI.
	 * Creating two wrappers for the same instanceURI is legal.
	 * @param model RDF2GO Model implementation, see http://rdf2go.ontoware.org
	 * @param instanceIdentifier an RDF2Go Resource identifying this instance
	 * @param write if true, the statement (this, rdf:type, TYPE) is written to the model
	 *
	 * [Generated from RDFReactor template rule #c2] 
	 */
	public SynchronizedTextElement ( Model model, org.ontoware.rdf2go.model.node.Resource instanceIdentifier, boolean write ) {
		super(model, RDFS_CLASS, instanceIdentifier, write);
	}


	/**
	 * Returns a Java wrapper over an RDF object, identified by a URI, given as a String.
	 * Creating two wrappers for the same URI is legal.
	 * @param model RDF2GO Model implementation, see http://rdf2go.ontoware.org
	 * @param uriString a URI given as a String
	 * @param write if true, the statement (this, rdf:type, TYPE) is written to the model
	 * @throws ModelRuntimeException if URI syntax is wrong
	 *
	 * [Generated from RDFReactor template rule #c7] 
	 */
	public SynchronizedTextElement ( Model model, String uriString, boolean write) throws ModelRuntimeException {
		super(model, RDFS_CLASS, new URIImpl(uriString,false), write);
	}

	/**
	 * Returns a Java wrapper over an RDF object, identified by a blank node.
	 * Creating two wrappers for the same blank node is legal.
	 * @param model RDF2GO Model implementation, see http://rdf2go.ontoware.org
	 * @param bnode BlankNode of this instance
	 * @param write if true, the statement (this, rdf:type, TYPE) is written to the model
	 *
	 * [Generated from RDFReactor template rule #c8] 
	 */
	public SynchronizedTextElement ( Model model, BlankNode bnode, boolean write ) {
		super(model, RDFS_CLASS, bnode, write);
	}

	/**
	 * Returns a Java wrapper over an RDF object, identified by 
	 * a randomly generated URI.
	 * Creating two wrappers results in different URIs.
	 * @param model RDF2GO Model implementation, see http://rdf2go.ontoware.org
	 * @param write if true, the statement (this, rdf:type, TYPE) is written to the model
	 *
	 * [Generated from RDFReactor template rule #c9] 
	 */
	public SynchronizedTextElement ( Model model, boolean write ) {
		super(model, RDFS_CLASS, model.newRandomUniqueURI(), write);
	}

    ///////////////////////////////////////////////////////////////////
    // typing

	/**
	 * Return an existing instance of this class in the model. No statements are written.
	 * @param model an RDF2Go model
	 * @param instanceResource an RDF2Go resource
	 * @return an instance of SynchronizedTextElement  or null if none existst
	 *
	 * [Generated from RDFReactor template rule #class0] 
	 */
	public static SynchronizedTextElement  getInstance(Model model, org.ontoware.rdf2go.model.node.Resource instanceResource) {
		return Base.getInstance(model, instanceResource, SynchronizedTextElement.class);
	}

	/**
	 * Create a new instance of this class in the model. 
	 * That is, create the statement (instanceResource, RDF.type, http://www.semanticdesktop.org/ontologies/2007/05/10/nid3#SynchronizedTextElement).
	 * @param model an RDF2Go model
	 * @param instanceResource an RDF2Go resource
	 *
	 * [Generated from RDFReactor template rule #class1] 
	 */
	public static void createInstance(Model model, org.ontoware.rdf2go.model.node.Resource instanceResource) {
		Base.createInstance(model, RDFS_CLASS, instanceResource);
	}

	/**
	 * @param model an RDF2Go model
	 * @param instanceResource an RDF2Go resource
	 * @return true if instanceResource is an instance of this class in the model
	 *
	 * [Generated from RDFReactor template rule #class2] 
	 */
	public static boolean hasInstance(Model model, org.ontoware.rdf2go.model.node.Resource instanceResource) {
		return Base.hasInstance(model, RDFS_CLASS, instanceResource);
	}

	/**
	 * @param model an RDF2Go model
	 * @return all instances of this class in Model 'model' as RDF resources
	 *
	 * [Generated from RDFReactor template rule #class3] 
	 */
	public static ClosableIterator<org.ontoware.rdf2go.model.node.Resource> getAllInstances(Model model) {
		return Base.getAllInstances(model, RDFS_CLASS, org.ontoware.rdf2go.model.node.Resource.class);
	}

	/**
	 * @param model an RDF2Go model
	 * @return all instances of this class in Model 'model' as a ReactorResult,
	 * which can conveniently be converted to iterator, list or array.
	 *
	 * [Generated from RDFReactor template rule #class3-as] 
	 */
	public static ReactorResult<? extends SynchronizedTextElement> getAllInstances_as(Model model) {
		return Base.getAllInstances_as(model, RDFS_CLASS, SynchronizedTextElement.class );
	}

    /**
	 * Remove rdf:type SynchronizedTextElement from this instance. Other triples are not affected.
	 * To delete more, use deleteAllProperties
	 * @param model an RDF2Go model
	 * @param instanceResource an RDF2Go resource
	 *
	 * [Generated from RDFReactor template rule #class4] 
	 */
	public static void deleteInstance(Model model, org.ontoware.rdf2go.model.node.Resource instanceResource) {
		Base.deleteInstance(model, RDFS_CLASS, instanceResource);
	}

	/**
	 * Delete all (this, *, *), i.e. including rdf:type
	 * @param model an RDF2Go model
	 * @param resource
	 */
	public static void deleteAllProperties(Model model,	org.ontoware.rdf2go.model.node.Resource instanceResource) {
		Base.deleteAllProperties(model, instanceResource);
	}

    ///////////////////////////////////////////////////////////////////
    // property access methods

	/**
	 * @param model an RDF2Go model
	 * @param objectValue
	 * @return all A's as RDF resources, that have a relation 'HasSynchronizedTextElement' to this SynchronizedTextElement instance
	 *
	 * [Generated from RDFReactor template rule #getallinverse1static] 
	 */
	public static ClosableIterator<org.ontoware.rdf2go.model.node.Resource> getAllHasSynchronizedTextElement_Inverse( Model model, Object objectValue) {
		return Base.getAll_Inverse(model, SynchronizedText.HASSYNCHRONIZEDTEXTELEMENT, objectValue);
	}

	/**
	 * @return all A's as RDF resources, that have a relation 'HasSynchronizedTextElement' to this SynchronizedTextElement instance
	 *
	 * [Generated from RDFReactor template rule #getallinverse1dynamic] 
	 */
	public ClosableIterator<org.ontoware.rdf2go.model.node.Resource> getAllHasSynchronizedTextElement_Inverse() {
		return Base.getAll_Inverse(this.model, SynchronizedText.HASSYNCHRONIZEDTEXTELEMENT, this.getResource() );
	}

	/**
	 * @param model an RDF2Go model
	 * @param objectValue
	 * @return all A's as a ReactorResult, that have a relation 'HasSynchronizedTextElement' to this SynchronizedTextElement instance
	 *
	 * [Generated from RDFReactor template rule #getallinverse-as1static] 
	 */
	public static ReactorResult<org.ontoware.rdf2go.model.node.Resource> getAllHasSynchronizedTextElement_Inverse_as( Model model, Object objectValue) {
		return Base.getAll_Inverse_as(model, SynchronizedText.HASSYNCHRONIZEDTEXTELEMENT, objectValue, org.ontoware.rdf2go.model.node.Resource.class);
	}



    /**
     * Check if org.ontoware.rdfreactor.generator.java.JProperty@d6655a7 has at least one value set 
     * @param model an RDF2Go model
     * @param resource an RDF2Go resource
     * @return true if this property has at least one value
	 *
	 * [Generated from RDFReactor template rule #get0has-static] 
     */
	public static boolean hasTextElementContent(Model model, org.ontoware.rdf2go.model.node.Resource instanceResource) {
		return Base.has(model, instanceResource, TEXTELEMENTCONTENT);
	}

    /**
     * Check if org.ontoware.rdfreactor.generator.java.JProperty@d6655a7 has at least one value set 
     * @return true if this property has at least one value
	 *
	 * [Generated from RDFReactor template rule #get0has-dynamic] 
     */
	public boolean hasTextElementContent() {
		return Base.has(this.model, this.getResource(), TEXTELEMENTCONTENT);
	}

    /**
     * Check if org.ontoware.rdfreactor.generator.java.JProperty@d6655a7 has the given value (maybe among other values).  
     * @param model an RDF2Go model
     * @param resource an RDF2Go resource
	 * @param value the value to be checked
     * @return true if this property contains (maybe among other) the given value
	 *
	 * [Generated from RDFReactor template rule #get0has-value-static] 
     */
	public static boolean hasTextElementContent(Model model, org.ontoware.rdf2go.model.node.Resource instanceResource, org.ontoware.rdf2go.model.node.Node value ) {
		return Base.hasValue(model, instanceResource, TEXTELEMENTCONTENT);
	}

    /**
     * Check if org.ontoware.rdfreactor.generator.java.JProperty@d6655a7 has the given value (maybe among other values).  
	 * @param value the value to be checked
     * @return true if this property contains (maybe among other) the given value
	 *
	 * [Generated from RDFReactor template rule #get0has-value-dynamic] 
     */
	public boolean hasTextElementContent( org.ontoware.rdf2go.model.node.Node value ) {
		return Base.hasValue(this.model, this.getResource(), TEXTELEMENTCONTENT);
	}

     /**
     * Get all values of property TextElementContent as an Iterator over RDF2Go nodes 
     * @param model an RDF2Go model
     * @param resource an RDF2Go resource
     * @return a ClosableIterator of RDF2Go Nodes
	 *
	 * [Generated from RDFReactor template rule #get7static] 
     */
	public static ClosableIterator<org.ontoware.rdf2go.model.node.Node> getAllTextElementContent_asNode(Model model, org.ontoware.rdf2go.model.node.Resource instanceResource) {
		return Base.getAll_asNode(model, instanceResource, TEXTELEMENTCONTENT);
	}
	
    /**
     * Get all values of property TextElementContent as a ReactorResult of RDF2Go nodes 
     * @param model an RDF2Go model
     * @param resource an RDF2Go resource
     * @return a List of RDF2Go Nodes
	 *
	 * [Generated from RDFReactor template rule #get7static-reactor-result] 
     */
	public static ReactorResult<org.ontoware.rdf2go.model.node.Node> getAllTextElementContent_asNode_(Model model, org.ontoware.rdf2go.model.node.Resource instanceResource) {
		return Base.getAll_as(model, instanceResource, TEXTELEMENTCONTENT, org.ontoware.rdf2go.model.node.Node.class);
	}

    /**
     * Get all values of property TextElementContent as an Iterator over RDF2Go nodes 
     * @return a ClosableIterator of RDF2Go Nodes
	 *
	 * [Generated from RDFReactor template rule #get8dynamic] 
     */
	public ClosableIterator<org.ontoware.rdf2go.model.node.Node> getAllTextElementContent_asNode() {
		return Base.getAll_asNode(this.model, this.getResource(), TEXTELEMENTCONTENT);
	}

    /**
     * Get all values of property TextElementContent as a ReactorResult of RDF2Go nodes 
     * @return a List of RDF2Go Nodes
	 *
	 * [Generated from RDFReactor template rule #get8dynamic-reactor-result] 
     */
	public ReactorResult<org.ontoware.rdf2go.model.node.Node> getAllTextElementContent_asNode_() {
		return Base.getAll_as(this.model, this.getResource(), TEXTELEMENTCONTENT, org.ontoware.rdf2go.model.node.Node.class);
	}
     /**
     * Get all values of property TextElementContent     * @param model an RDF2Go model
     * @param resource an RDF2Go resource
     * @return a ClosableIterator of $type
	 *
	 * [Generated from RDFReactor template rule #get11static] 
     */
	public static ClosableIterator<java.lang.String> getAllTextElementContent(Model model, org.ontoware.rdf2go.model.node.Resource instanceResource) {
		return Base.getAll(model, instanceResource, TEXTELEMENTCONTENT, java.lang.String.class);
	}
	
    /**
     * Get all values of property TextElementContent as a ReactorResult of java.lang.String 
     * @param model an RDF2Go model
     * @param resource an RDF2Go resource
     * @return a ReactorResult of $type which can conveniently be converted to iterator, list or array
	 *
	 * [Generated from RDFReactor template rule #get11static-reactorresult] 
     */
	public static ReactorResult<java.lang.String> getAllTextElementContent_as(Model model, org.ontoware.rdf2go.model.node.Resource instanceResource) {
		return Base.getAll_as(model, instanceResource, TEXTELEMENTCONTENT, java.lang.String.class);
	}

    /**
     * Get all values of property TextElementContent     * @return a ClosableIterator of $type
	 *
	 * [Generated from RDFReactor template rule #get12dynamic] 
     */
	public ClosableIterator<java.lang.String> getAllTextElementContent() {
		return Base.getAll(this.model, this.getResource(), TEXTELEMENTCONTENT, java.lang.String.class);
	}

    /**
     * Get all values of property TextElementContent as a ReactorResult of java.lang.String 
     * @return a ReactorResult of $type which can conveniently be converted to iterator, list or array
	 *
	 * [Generated from RDFReactor template rule #get12dynamic-reactorresult] 
     */
	public ReactorResult<java.lang.String> getAllTextElementContent_as() {
		return Base.getAll_as(this.model, this.getResource(), TEXTELEMENTCONTENT, java.lang.String.class);
	}
 
    /**
     * Adds a value to property TextElementContent as an RDF2Go node 
     * @param model an RDF2Go model
     * @param resource an RDF2Go resource
	 * @param value the value to be added
	 *
	 * [Generated from RDFReactor template rule #add1static] 
     */
	public static void addTextElementContent( Model model, org.ontoware.rdf2go.model.node.Resource instanceResource, org.ontoware.rdf2go.model.node.Node value) {
		Base.add(model, instanceResource, TEXTELEMENTCONTENT, value);
	}
	
    /**
     * Adds a value to property TextElementContent as an RDF2Go node 
	 * @param value the value to be added
	 *
	 * [Generated from RDFReactor template rule #add1dynamic] 
     */
	public void addTextElementContent( org.ontoware.rdf2go.model.node.Node value) {
		Base.add(this.model, this.getResource(), TEXTELEMENTCONTENT, value);
	}
    /**
     * Adds a value to property TextElementContent from an instance of java.lang.String 
     * @param model an RDF2Go model
     * @param resource an RDF2Go resource
	 *
	 * [Generated from RDFReactor template rule #add3static] 
     */
	public static void addTextElementContent(Model model, org.ontoware.rdf2go.model.node.Resource instanceResource, java.lang.String value) {
		Base.add(model, instanceResource, TEXTELEMENTCONTENT, value);
	}
	
    /**
     * Adds a value to property TextElementContent from an instance of java.lang.String 
	 *
	 * [Generated from RDFReactor template rule #add4dynamic] 
     */
	public void addTextElementContent(java.lang.String value) {
		Base.add(this.model, this.getResource(), TEXTELEMENTCONTENT, value);
	}
  

    /**
     * Sets a value of property TextElementContent from an RDF2Go node.
     * First, all existing values are removed, then this value is added.
     * Cardinality constraints are not checked, but this method exists only for properties with
     * no minCardinality or minCardinality == 1.
     * @param model an RDF2Go model
     * @param resource an RDF2Go resource
	 * @param value the value to be set
	 *
	 * [Generated from RDFReactor template rule #set1static] 
     */
	public static void setTextElementContent( Model model, org.ontoware.rdf2go.model.node.Resource instanceResource, org.ontoware.rdf2go.model.node.Node value) {
		Base.set(model, instanceResource, TEXTELEMENTCONTENT, value);
	}
	
    /**
     * Sets a value of property TextElementContent from an RDF2Go node.
     * First, all existing values are removed, then this value is added.
     * Cardinality constraints are not checked, but this method exists only for properties with
     * no minCardinality or minCardinality == 1.
	 * @param value the value to be added
	 *
	 * [Generated from RDFReactor template rule #set1dynamic] 
     */
	public void setTextElementContent( org.ontoware.rdf2go.model.node.Node value) {
		Base.set(this.model, this.getResource(), TEXTELEMENTCONTENT, value);
	}
    /**
     * Sets a value of property TextElementContent from an instance of java.lang.String 
     * First, all existing values are removed, then this value is added.
     * Cardinality constraints are not checked, but this method exists only for properties with
     * no minCardinality or minCardinality == 1.
     * @param model an RDF2Go model
     * @param resource an RDF2Go resource
	 * @param value the value to be added
	 *
	 * [Generated from RDFReactor template rule #set3static] 
     */
	public static void setTextElementContent(Model model, org.ontoware.rdf2go.model.node.Resource instanceResource, java.lang.String value) {
		Base.set(model, instanceResource, TEXTELEMENTCONTENT, value);
	}
	
    /**
     * Sets a value of property TextElementContent from an instance of java.lang.String 
     * First, all existing values are removed, then this value is added.
     * Cardinality constraints are not checked, but this method exists only for properties with
     * no minCardinality or minCardinality == 1.
	 * @param value the value to be added
	 *
	 * [Generated from RDFReactor template rule #set4dynamic] 
     */
	public void setTextElementContent(java.lang.String value) {
		Base.set(this.model, this.getResource(), TEXTELEMENTCONTENT, value);
	}
  


    /**
     * Removes a value of property TextElementContent as an RDF2Go node 
     * @param model an RDF2Go model
     * @param resource an RDF2Go resource
	 * @param value the value to be removed
	 *
	 * [Generated from RDFReactor template rule #remove1static] 
     */
	public static void removeTextElementContent( Model model, org.ontoware.rdf2go.model.node.Resource instanceResource, org.ontoware.rdf2go.model.node.Node value) {
		Base.remove(model, instanceResource, TEXTELEMENTCONTENT, value);
	}
	
    /**
     * Removes a value of property TextElementContent as an RDF2Go node
	 * @param value the value to be removed
	 *
	 * [Generated from RDFReactor template rule #remove1dynamic] 
     */
	public void removeTextElementContent( org.ontoware.rdf2go.model.node.Node value) {
		Base.remove(this.model, this.getResource(), TEXTELEMENTCONTENT, value);
	}
    /**
     * Removes a value of property TextElementContent given as an instance of java.lang.String 
     * @param model an RDF2Go model
     * @param resource an RDF2Go resource
	 * @param value the value to be removed
	 *
	 * [Generated from RDFReactor template rule #remove3static] 
     */
	public static void removeTextElementContent(Model model, org.ontoware.rdf2go.model.node.Resource instanceResource, java.lang.String value) {
		Base.remove(model, instanceResource, TEXTELEMENTCONTENT, value);
	}
	
    /**
     * Removes a value of property TextElementContent given as an instance of java.lang.String 
	 * @param value the value to be removed
	 *
	 * [Generated from RDFReactor template rule #remove4dynamic] 
     */
	public void removeTextElementContent(java.lang.String value) {
		Base.remove(this.model, this.getResource(), TEXTELEMENTCONTENT, value);
	}
  
    /**
     * Removes all values of property TextElementContent     * @param model an RDF2Go model
     * @param resource an RDF2Go resource
	 *
	 * [Generated from RDFReactor template rule #removeall1static] 
     */
	public static void removeAllTextElementContent( Model model, org.ontoware.rdf2go.model.node.Resource instanceResource) {
		Base.removeAll(model, instanceResource, TEXTELEMENTCONTENT);
	}
	
    /**
     * Removes all values of property TextElementContent	 *
	 * [Generated from RDFReactor template rule #removeall1dynamic] 
     */
	public void removeAllTextElementContent() {
		Base.removeAll(this.model, this.getResource(), TEXTELEMENTCONTENT);
	}
     /**
     * Check if org.ontoware.rdfreactor.generator.java.JProperty@6ffeea62 has at least one value set 
     * @param model an RDF2Go model
     * @param resource an RDF2Go resource
     * @return true if this property has at least one value
	 *
	 * [Generated from RDFReactor template rule #get0has-static] 
     */
	public static boolean hasTextElementTimestamp(Model model, org.ontoware.rdf2go.model.node.Resource instanceResource) {
		return Base.has(model, instanceResource, TEXTELEMENTTIMESTAMP);
	}

    /**
     * Check if org.ontoware.rdfreactor.generator.java.JProperty@6ffeea62 has at least one value set 
     * @return true if this property has at least one value
	 *
	 * [Generated from RDFReactor template rule #get0has-dynamic] 
     */
	public boolean hasTextElementTimestamp() {
		return Base.has(this.model, this.getResource(), TEXTELEMENTTIMESTAMP);
	}

    /**
     * Check if org.ontoware.rdfreactor.generator.java.JProperty@6ffeea62 has the given value (maybe among other values).  
     * @param model an RDF2Go model
     * @param resource an RDF2Go resource
	 * @param value the value to be checked
     * @return true if this property contains (maybe among other) the given value
	 *
	 * [Generated from RDFReactor template rule #get0has-value-static] 
     */
	public static boolean hasTextElementTimestamp(Model model, org.ontoware.rdf2go.model.node.Resource instanceResource, org.ontoware.rdf2go.model.node.Node value ) {
		return Base.hasValue(model, instanceResource, TEXTELEMENTTIMESTAMP);
	}

    /**
     * Check if org.ontoware.rdfreactor.generator.java.JProperty@6ffeea62 has the given value (maybe among other values).  
	 * @param value the value to be checked
     * @return true if this property contains (maybe among other) the given value
	 *
	 * [Generated from RDFReactor template rule #get0has-value-dynamic] 
     */
	public boolean hasTextElementTimestamp( org.ontoware.rdf2go.model.node.Node value ) {
		return Base.hasValue(this.model, this.getResource(), TEXTELEMENTTIMESTAMP);
	}

     /**
     * Get all values of property TextElementTimestamp as an Iterator over RDF2Go nodes 
     * @param model an RDF2Go model
     * @param resource an RDF2Go resource
     * @return a ClosableIterator of RDF2Go Nodes
	 *
	 * [Generated from RDFReactor template rule #get7static] 
     */
	public static ClosableIterator<org.ontoware.rdf2go.model.node.Node> getAllTextElementTimestamp_asNode(Model model, org.ontoware.rdf2go.model.node.Resource instanceResource) {
		return Base.getAll_asNode(model, instanceResource, TEXTELEMENTTIMESTAMP);
	}
	
    /**
     * Get all values of property TextElementTimestamp as a ReactorResult of RDF2Go nodes 
     * @param model an RDF2Go model
     * @param resource an RDF2Go resource
     * @return a List of RDF2Go Nodes
	 *
	 * [Generated from RDFReactor template rule #get7static-reactor-result] 
     */
	public static ReactorResult<org.ontoware.rdf2go.model.node.Node> getAllTextElementTimestamp_asNode_(Model model, org.ontoware.rdf2go.model.node.Resource instanceResource) {
		return Base.getAll_as(model, instanceResource, TEXTELEMENTTIMESTAMP, org.ontoware.rdf2go.model.node.Node.class);
	}

    /**
     * Get all values of property TextElementTimestamp as an Iterator over RDF2Go nodes 
     * @return a ClosableIterator of RDF2Go Nodes
	 *
	 * [Generated from RDFReactor template rule #get8dynamic] 
     */
	public ClosableIterator<org.ontoware.rdf2go.model.node.Node> getAllTextElementTimestamp_asNode() {
		return Base.getAll_asNode(this.model, this.getResource(), TEXTELEMENTTIMESTAMP);
	}

    /**
     * Get all values of property TextElementTimestamp as a ReactorResult of RDF2Go nodes 
     * @return a List of RDF2Go Nodes
	 *
	 * [Generated from RDFReactor template rule #get8dynamic-reactor-result] 
     */
	public ReactorResult<org.ontoware.rdf2go.model.node.Node> getAllTextElementTimestamp_asNode_() {
		return Base.getAll_as(this.model, this.getResource(), TEXTELEMENTTIMESTAMP, org.ontoware.rdf2go.model.node.Node.class);
	}
     /**
     * Get all values of property TextElementTimestamp     * @param model an RDF2Go model
     * @param resource an RDF2Go resource
     * @return a ClosableIterator of $type
	 *
	 * [Generated from RDFReactor template rule #get11static] 
     */
	public static ClosableIterator<java.lang.Integer> getAllTextElementTimestamp(Model model, org.ontoware.rdf2go.model.node.Resource instanceResource) {
		return Base.getAll(model, instanceResource, TEXTELEMENTTIMESTAMP, java.lang.Integer.class);
	}
	
    /**
     * Get all values of property TextElementTimestamp as a ReactorResult of java.lang.Integer 
     * @param model an RDF2Go model
     * @param resource an RDF2Go resource
     * @return a ReactorResult of $type which can conveniently be converted to iterator, list or array
	 *
	 * [Generated from RDFReactor template rule #get11static-reactorresult] 
     */
	public static ReactorResult<java.lang.Integer> getAllTextElementTimestamp_as(Model model, org.ontoware.rdf2go.model.node.Resource instanceResource) {
		return Base.getAll_as(model, instanceResource, TEXTELEMENTTIMESTAMP, java.lang.Integer.class);
	}

    /**
     * Get all values of property TextElementTimestamp     * @return a ClosableIterator of $type
	 *
	 * [Generated from RDFReactor template rule #get12dynamic] 
     */
	public ClosableIterator<java.lang.Integer> getAllTextElementTimestamp() {
		return Base.getAll(this.model, this.getResource(), TEXTELEMENTTIMESTAMP, java.lang.Integer.class);
	}

    /**
     * Get all values of property TextElementTimestamp as a ReactorResult of java.lang.Integer 
     * @return a ReactorResult of $type which can conveniently be converted to iterator, list or array
	 *
	 * [Generated from RDFReactor template rule #get12dynamic-reactorresult] 
     */
	public ReactorResult<java.lang.Integer> getAllTextElementTimestamp_as() {
		return Base.getAll_as(this.model, this.getResource(), TEXTELEMENTTIMESTAMP, java.lang.Integer.class);
	}
 
    /**
     * Adds a value to property TextElementTimestamp as an RDF2Go node 
     * @param model an RDF2Go model
     * @param resource an RDF2Go resource
	 * @param value the value to be added
	 *
	 * [Generated from RDFReactor template rule #add1static] 
     */
	public static void addTextElementTimestamp( Model model, org.ontoware.rdf2go.model.node.Resource instanceResource, org.ontoware.rdf2go.model.node.Node value) {
		Base.add(model, instanceResource, TEXTELEMENTTIMESTAMP, value);
	}
	
    /**
     * Adds a value to property TextElementTimestamp as an RDF2Go node 
	 * @param value the value to be added
	 *
	 * [Generated from RDFReactor template rule #add1dynamic] 
     */
	public void addTextElementTimestamp( org.ontoware.rdf2go.model.node.Node value) {
		Base.add(this.model, this.getResource(), TEXTELEMENTTIMESTAMP, value);
	}
    /**
     * Adds a value to property TextElementTimestamp from an instance of java.lang.Integer 
     * @param model an RDF2Go model
     * @param resource an RDF2Go resource
	 *
	 * [Generated from RDFReactor template rule #add3static] 
     */
	public static void addTextElementTimestamp(Model model, org.ontoware.rdf2go.model.node.Resource instanceResource, java.lang.Integer value) {
		Base.add(model, instanceResource, TEXTELEMENTTIMESTAMP, value);
	}
	
    /**
     * Adds a value to property TextElementTimestamp from an instance of java.lang.Integer 
	 *
	 * [Generated from RDFReactor template rule #add4dynamic] 
     */
	public void addTextElementTimestamp(java.lang.Integer value) {
		Base.add(this.model, this.getResource(), TEXTELEMENTTIMESTAMP, value);
	}
  

    /**
     * Sets a value of property TextElementTimestamp from an RDF2Go node.
     * First, all existing values are removed, then this value is added.
     * Cardinality constraints are not checked, but this method exists only for properties with
     * no minCardinality or minCardinality == 1.
     * @param model an RDF2Go model
     * @param resource an RDF2Go resource
	 * @param value the value to be set
	 *
	 * [Generated from RDFReactor template rule #set1static] 
     */
	public static void setTextElementTimestamp( Model model, org.ontoware.rdf2go.model.node.Resource instanceResource, org.ontoware.rdf2go.model.node.Node value) {
		Base.set(model, instanceResource, TEXTELEMENTTIMESTAMP, value);
	}
	
    /**
     * Sets a value of property TextElementTimestamp from an RDF2Go node.
     * First, all existing values are removed, then this value is added.
     * Cardinality constraints are not checked, but this method exists only for properties with
     * no minCardinality or minCardinality == 1.
	 * @param value the value to be added
	 *
	 * [Generated from RDFReactor template rule #set1dynamic] 
     */
	public void setTextElementTimestamp( org.ontoware.rdf2go.model.node.Node value) {
		Base.set(this.model, this.getResource(), TEXTELEMENTTIMESTAMP, value);
	}
    /**
     * Sets a value of property TextElementTimestamp from an instance of java.lang.Integer 
     * First, all existing values are removed, then this value is added.
     * Cardinality constraints are not checked, but this method exists only for properties with
     * no minCardinality or minCardinality == 1.
     * @param model an RDF2Go model
     * @param resource an RDF2Go resource
	 * @param value the value to be added
	 *
	 * [Generated from RDFReactor template rule #set3static] 
     */
	public static void setTextElementTimestamp(Model model, org.ontoware.rdf2go.model.node.Resource instanceResource, java.lang.Integer value) {
		Base.set(model, instanceResource, TEXTELEMENTTIMESTAMP, value);
	}
	
    /**
     * Sets a value of property TextElementTimestamp from an instance of java.lang.Integer 
     * First, all existing values are removed, then this value is added.
     * Cardinality constraints are not checked, but this method exists only for properties with
     * no minCardinality or minCardinality == 1.
	 * @param value the value to be added
	 *
	 * [Generated from RDFReactor template rule #set4dynamic] 
     */
	public void setTextElementTimestamp(java.lang.Integer value) {
		Base.set(this.model, this.getResource(), TEXTELEMENTTIMESTAMP, value);
	}
  


    /**
     * Removes a value of property TextElementTimestamp as an RDF2Go node 
     * @param model an RDF2Go model
     * @param resource an RDF2Go resource
	 * @param value the value to be removed
	 *
	 * [Generated from RDFReactor template rule #remove1static] 
     */
	public static void removeTextElementTimestamp( Model model, org.ontoware.rdf2go.model.node.Resource instanceResource, org.ontoware.rdf2go.model.node.Node value) {
		Base.remove(model, instanceResource, TEXTELEMENTTIMESTAMP, value);
	}
	
    /**
     * Removes a value of property TextElementTimestamp as an RDF2Go node
	 * @param value the value to be removed
	 *
	 * [Generated from RDFReactor template rule #remove1dynamic] 
     */
	public void removeTextElementTimestamp( org.ontoware.rdf2go.model.node.Node value) {
		Base.remove(this.model, this.getResource(), TEXTELEMENTTIMESTAMP, value);
	}
    /**
     * Removes a value of property TextElementTimestamp given as an instance of java.lang.Integer 
     * @param model an RDF2Go model
     * @param resource an RDF2Go resource
	 * @param value the value to be removed
	 *
	 * [Generated from RDFReactor template rule #remove3static] 
     */
	public static void removeTextElementTimestamp(Model model, org.ontoware.rdf2go.model.node.Resource instanceResource, java.lang.Integer value) {
		Base.remove(model, instanceResource, TEXTELEMENTTIMESTAMP, value);
	}
	
    /**
     * Removes a value of property TextElementTimestamp given as an instance of java.lang.Integer 
	 * @param value the value to be removed
	 *
	 * [Generated from RDFReactor template rule #remove4dynamic] 
     */
	public void removeTextElementTimestamp(java.lang.Integer value) {
		Base.remove(this.model, this.getResource(), TEXTELEMENTTIMESTAMP, value);
	}
  
    /**
     * Removes all values of property TextElementTimestamp     * @param model an RDF2Go model
     * @param resource an RDF2Go resource
	 *
	 * [Generated from RDFReactor template rule #removeall1static] 
     */
	public static void removeAllTextElementTimestamp( Model model, org.ontoware.rdf2go.model.node.Resource instanceResource) {
		Base.removeAll(model, instanceResource, TEXTELEMENTTIMESTAMP);
	}
	
    /**
     * Removes all values of property TextElementTimestamp	 *
	 * [Generated from RDFReactor template rule #removeall1dynamic] 
     */
	public void removeAllTextElementTimestamp() {
		Base.removeAll(this.model, this.getResource(), TEXTELEMENTTIMESTAMP);
	}
 }