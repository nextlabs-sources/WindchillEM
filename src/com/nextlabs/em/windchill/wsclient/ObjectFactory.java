
package com.nextlabs.em.windchill.wsclient;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.nextlabs.em.windchill.wsclient package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _QueryUserResponse_QNAME = new QName("http://windchill.em.nextlabs.com/", "QueryUserResponse");
    private final static QName _QueryUser_QNAME = new QName("http://windchill.em.nextlabs.com/", "QueryUser");
    private final static QName _QueryTestResponse_QNAME = new QName("http://windchill.em.nextlabs.com/", "QueryTestResponse");
    private final static QName _ObjectAttrCollection_QNAME = new QName("http://windchill.em.nextlabs.com/", "objectAttrCollection");
    private final static QName _QueryOidResponse_QNAME = new QName("http://windchill.em.nextlabs.com/", "QueryOidResponse");
    private final static QName _EvalResponse_QNAME = new QName("http://windchill.em.nextlabs.com/", "EvalResponse");
    private final static QName _QueryTest_QNAME = new QName("http://windchill.em.nextlabs.com/", "QueryTest");
    private final static QName _QueryOid_QNAME = new QName("http://windchill.em.nextlabs.com/", "QueryOid");
    private final static QName _Eval_QNAME = new QName("http://windchill.em.nextlabs.com/", "Eval");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.nextlabs.em.windchill.wsclient
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ObjectAttrCollection }
     * 
     */
    public ObjectAttrCollection createObjectAttrCollection() {
        return new ObjectAttrCollection();
    }

    /**
     * Create an instance of {@link QueryUserResponse }
     * 
     */
    public QueryUserResponse createQueryUserResponse() {
        return new QueryUserResponse();
    }

    /**
     * Create an instance of {@link EvalResponse }
     * 
     */
    public EvalResponse createEvalResponse() {
        return new EvalResponse();
    }

    /**
     * Create an instance of {@link MapEntry }
     * 
     */
    public MapEntry createMapEntry() {
        return new MapEntry();
    }

    /**
     * Create an instance of {@link Eval }
     * 
     */
    public Eval createEval() {
        return new Eval();
    }

    /**
     * Create an instance of {@link QueryUser }
     * 
     */
    public QueryUser createQueryUser() {
        return new QueryUser();
    }

    /**
     * Create an instance of {@link MapConvertor }
     * 
     */
    public MapConvertor createMapConvertor() {
        return new MapConvertor();
    }

    /**
     * Create an instance of {@link QueryTestResponse }
     * 
     */
    public QueryTestResponse createQueryTestResponse() {
        return new QueryTestResponse();
    }

    /**
     * Create an instance of {@link QueryOid }
     * 
     */
    public QueryOid createQueryOid() {
        return new QueryOid();
    }

    /**
     * Create an instance of {@link QueryTest }
     * 
     */
    public QueryTest createQueryTest() {
        return new QueryTest();
    }

    /**
     * Create an instance of {@link QueryOidResponse }
     * 
     */
    public QueryOidResponse createQueryOidResponse() {
        return new QueryOidResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryUserResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://windchill.em.nextlabs.com/", name = "QueryUserResponse")
    public JAXBElement<QueryUserResponse> createQueryUserResponse(QueryUserResponse value) {
        return new JAXBElement<QueryUserResponse>(_QueryUserResponse_QNAME, QueryUserResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryUser }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://windchill.em.nextlabs.com/", name = "QueryUser")
    public JAXBElement<QueryUser> createQueryUser(QueryUser value) {
        return new JAXBElement<QueryUser>(_QueryUser_QNAME, QueryUser.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryTestResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://windchill.em.nextlabs.com/", name = "QueryTestResponse")
    public JAXBElement<QueryTestResponse> createQueryTestResponse(QueryTestResponse value) {
        return new JAXBElement<QueryTestResponse>(_QueryTestResponse_QNAME, QueryTestResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ObjectAttrCollection }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://windchill.em.nextlabs.com/", name = "objectAttrCollection")
    public JAXBElement<ObjectAttrCollection> createObjectAttrCollection(ObjectAttrCollection value) {
        return new JAXBElement<ObjectAttrCollection>(_ObjectAttrCollection_QNAME, ObjectAttrCollection.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryOidResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://windchill.em.nextlabs.com/", name = "QueryOidResponse")
    public JAXBElement<QueryOidResponse> createQueryOidResponse(QueryOidResponse value) {
        return new JAXBElement<QueryOidResponse>(_QueryOidResponse_QNAME, QueryOidResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EvalResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://windchill.em.nextlabs.com/", name = "EvalResponse")
    public JAXBElement<EvalResponse> createEvalResponse(EvalResponse value) {
        return new JAXBElement<EvalResponse>(_EvalResponse_QNAME, EvalResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryTest }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://windchill.em.nextlabs.com/", name = "QueryTest")
    public JAXBElement<QueryTest> createQueryTest(QueryTest value) {
        return new JAXBElement<QueryTest>(_QueryTest_QNAME, QueryTest.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryOid }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://windchill.em.nextlabs.com/", name = "QueryOid")
    public JAXBElement<QueryOid> createQueryOid(QueryOid value) {
        return new JAXBElement<QueryOid>(_QueryOid_QNAME, QueryOid.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Eval }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://windchill.em.nextlabs.com/", name = "Eval")
    public JAXBElement<Eval> createEval(Eval value) {
        return new JAXBElement<Eval>(_Eval_QNAME, Eval.class, null, value);
    }

}
