/*******************************************************************************
 * Copyright (c) 2013 <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a>.
 * All rights reserved. This program, solace.common, and file, Databases.java, and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a> - initial API and implementation
 ******************************************************************************/
//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.05.21 at 06:37:52 PM EDT 
//


package com.solace.data;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="database" maxOccurs="unbounded">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="hostname" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="application" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="auth">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="username" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                             &lt;element name="password" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                           &lt;/sequence>
 *                           &lt;attribute name="trusted" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="pooling">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;attribute name="maxSize" type="{http://www.w3.org/2001/XMLSchema}int" />
 *                           &lt;attribute name="minSize" type="{http://www.w3.org/2001/XMLSchema}int" />
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="timeout">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;attribute name="connection" type="{http://www.w3.org/2001/XMLSchema}int" />
 *                           &lt;attribute name="command" type="{http://www.w3.org/2001/XMLSchema}int" />
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="handler">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="property">
 *                               &lt;complexType>
 *                                 &lt;complexContent>
 *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                     &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                                     &lt;attribute name="value" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                                   &lt;/restriction>
 *                                 &lt;/complexContent>
 *                               &lt;/complexType>
 *                             &lt;/element>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "database"
})
@XmlRootElement(name = "databases")
public class Databases {

    @XmlElement(required = true)
    protected List<Databases.DatabaseConfig> database;

    /**
     * Gets the value of the database property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the database property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDatabase().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Databases.DatabaseConfig }
     * 
     * 
     */
    public List<Databases.DatabaseConfig> getDatabase() {
        if (database == null) {
            database = new ArrayList<Databases.DatabaseConfig>();
        }
        return this.database;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="hostname" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="application" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="auth">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="username" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                   &lt;element name="password" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                 &lt;/sequence>
     *                 &lt;attribute name="trusted" type="{http://www.w3.org/2001/XMLSchema}boolean" />
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="pooling">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;attribute name="maxSize" type="{http://www.w3.org/2001/XMLSchema}int" />
     *                 &lt;attribute name="minSize" type="{http://www.w3.org/2001/XMLSchema}int" />
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="timeout">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;attribute name="connection" type="{http://www.w3.org/2001/XMLSchema}int" />
     *                 &lt;attribute name="command" type="{http://www.w3.org/2001/XMLSchema}int" />
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="handler">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="property">
     *                     &lt;complexType>
     *                       &lt;complexContent>
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                           &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" />
     *                           &lt;attribute name="value" type="{http://www.w3.org/2001/XMLSchema}string" />
     *                         &lt;/restriction>
     *                       &lt;/complexContent>
     *                     &lt;/complexType>
     *                   &lt;/element>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "hostname",
        "application",
        "auth",
        "pooling",
        "timeout",
        "handler"
    })
    public static class DatabaseConfig {

        @XmlElement(required = true)
        protected String hostname;
        @XmlElement(required = true)
        protected String application;
        @XmlElement(required = true)
        protected Databases.DatabaseConfig.Auth auth;
        @XmlElement(required = true)
        protected Databases.DatabaseConfig.Pooling pooling;
        @XmlElement(required = true)
        protected Databases.DatabaseConfig.Timeout timeout;
        @XmlElement(required = true)
        protected Databases.DatabaseConfig.Handler handler;

        /**
         * Gets the value of the hostname property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getHostname() {
            return hostname;
        }

        /**
         * Sets the value of the hostname property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setHostname(String value) {
            this.hostname = value;
        }

        /**
         * Gets the value of the application property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getApplication() {
            return application;
        }

        /**
         * Sets the value of the application property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setApplication(String value) {
            this.application = value;
        }

        /**
         * Gets the value of the auth property.
         * 
         * @return
         *     possible object is
         *     {@link Databases.DatabaseConfig.Auth }
         *     
         */
        public Databases.DatabaseConfig.Auth getAuth() {
            return auth;
        }

        /**
         * Sets the value of the auth property.
         * 
         * @param value
         *     allowed object is
         *     {@link Databases.DatabaseConfig.Auth }
         *     
         */
        public void setAuth(Databases.DatabaseConfig.Auth value) {
            this.auth = value;
        }

        /**
         * Gets the value of the pooling property.
         * 
         * @return
         *     possible object is
         *     {@link Databases.DatabaseConfig.Pooling }
         *     
         */
        public Databases.DatabaseConfig.Pooling getPooling() {
            return pooling;
        }

        /**
         * Sets the value of the pooling property.
         * 
         * @param value
         *     allowed object is
         *     {@link Databases.DatabaseConfig.Pooling }
         *     
         */
        public void setPooling(Databases.DatabaseConfig.Pooling value) {
            this.pooling = value;
        }

        /**
         * Gets the value of the timeout property.
         * 
         * @return
         *     possible object is
         *     {@link Databases.DatabaseConfig.Timeout }
         *     
         */
        public Databases.DatabaseConfig.Timeout getTimeout() {
            return timeout;
        }

        /**
         * Sets the value of the timeout property.
         * 
         * @param value
         *     allowed object is
         *     {@link Databases.DatabaseConfig.Timeout }
         *     
         */
        public void setTimeout(Databases.DatabaseConfig.Timeout value) {
            this.timeout = value;
        }

        /**
         * Gets the value of the handler property.
         * 
         * @return
         *     possible object is
         *     {@link Databases.DatabaseConfig.Handler }
         *     
         */
        public Databases.DatabaseConfig.Handler getHandler() {
            return handler;
        }

        /**
         * Sets the value of the handler property.
         * 
         * @param value
         *     allowed object is
         *     {@link Databases.DatabaseConfig.Handler }
         *     
         */
        public void setHandler(Databases.DatabaseConfig.Handler value) {
            this.handler = value;
        }


        /**
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;sequence>
         *         &lt;element name="username" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *         &lt;element name="password" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *       &lt;/sequence>
         *       &lt;attribute name="trusted" type="{http://www.w3.org/2001/XMLSchema}boolean" />
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "username",
            "password"
        })
        public static class Auth {

            @XmlElement(required = true)
            protected String username;
            @XmlElement(required = true)
            protected String password;
            @XmlAttribute
            protected Boolean trusted;

            /**
             * Gets the value of the username property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getUsername() {
                return username;
            }

            /**
             * Sets the value of the username property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setUsername(String value) {
                this.username = value;
            }

            /**
             * Gets the value of the password property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getPassword() {
                return password;
            }

            /**
             * Sets the value of the password property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setPassword(String value) {
                this.password = value;
            }

            /**
             * Gets the value of the trusted property.
             * 
             * @return
             *     possible object is
             *     {@link Boolean }
             *     
             */
            public Boolean isTrusted() {
                return trusted;
            }

            /**
             * Sets the value of the trusted property.
             * 
             * @param value
             *     allowed object is
             *     {@link Boolean }
             *     
             */
            public void setTrusted(Boolean value) {
                this.trusted = value;
            }

        }


        /**
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;sequence>
         *         &lt;element name="property">
         *           &lt;complexType>
         *             &lt;complexContent>
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                 &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" />
         *                 &lt;attribute name="value" type="{http://www.w3.org/2001/XMLSchema}string" />
         *               &lt;/restriction>
         *             &lt;/complexContent>
         *           &lt;/complexType>
         *         &lt;/element>
         *       &lt;/sequence>
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "property"
        })
        public static class Handler {

            @XmlElement(required = true)
            protected Databases.DatabaseConfig.Handler.Property property;

            /**
             * Gets the value of the property property.
             * 
             * @return
             *     possible object is
             *     {@link Databases.DatabaseConfig.Handler.Property }
             *     
             */
            public Databases.DatabaseConfig.Handler.Property getProperty() {
                return property;
            }

            /**
             * Sets the value of the property property.
             * 
             * @param value
             *     allowed object is
             *     {@link Databases.DatabaseConfig.Handler.Property }
             *     
             */
            public void setProperty(Databases.DatabaseConfig.Handler.Property value) {
                this.property = value;
            }


            /**
             * <p>Java class for anonymous complex type.
             * 
             * <p>The following schema fragment specifies the expected content contained within this class.
             * 
             * <pre>
             * &lt;complexType>
             *   &lt;complexContent>
             *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *       &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" />
             *       &lt;attribute name="value" type="{http://www.w3.org/2001/XMLSchema}string" />
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "")
            public static class Property {

                @XmlAttribute
                protected String name;
                @XmlAttribute
                protected String value;

                /**
                 * Gets the value of the name property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getName() {
                    return name;
                }

                /**
                 * Sets the value of the name property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setName(String value) {
                    this.name = value;
                }

                /**
                 * Gets the value of the value property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getValue() {
                    return value;
                }

                /**
                 * Sets the value of the value property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setValue(String value) {
                    this.value = value;
                }

            }

        }


        /**
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;attribute name="maxSize" type="{http://www.w3.org/2001/XMLSchema}int" />
         *       &lt;attribute name="minSize" type="{http://www.w3.org/2001/XMLSchema}int" />
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "")
        public static class Pooling {

            @XmlAttribute
            protected Integer maxSize;
            @XmlAttribute
            protected Integer minSize;

            /**
             * Gets the value of the maxSize property.
             * 
             * @return
             *     possible object is
             *     {@link Integer }
             *     
             */
            public Integer getMaxSize() {
                return maxSize;
            }

            /**
             * Sets the value of the maxSize property.
             * 
             * @param value
             *     allowed object is
             *     {@link Integer }
             *     
             */
            public void setMaxSize(Integer value) {
                this.maxSize = value;
            }

            /**
             * Gets the value of the minSize property.
             * 
             * @return
             *     possible object is
             *     {@link Integer }
             *     
             */
            public Integer getMinSize() {
                return minSize;
            }

            /**
             * Sets the value of the minSize property.
             * 
             * @param value
             *     allowed object is
             *     {@link Integer }
             *     
             */
            public void setMinSize(Integer value) {
                this.minSize = value;
            }

        }


        /**
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;attribute name="connection" type="{http://www.w3.org/2001/XMLSchema}int" />
         *       &lt;attribute name="command" type="{http://www.w3.org/2001/XMLSchema}int" />
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "")
        public static class Timeout {

            @XmlAttribute
            protected Integer connection;
            @XmlAttribute
            protected Integer command;

            /**
             * Gets the value of the connection property.
             * 
             * @return
             *     possible object is
             *     {@link Integer }
             *     
             */
            public Integer getConnection() {
                return connection;
            }

            /**
             * Sets the value of the connection property.
             * 
             * @param value
             *     allowed object is
             *     {@link Integer }
             *     
             */
            public void setConnection(Integer value) {
                this.connection = value;
            }

            /**
             * Gets the value of the command property.
             * 
             * @return
             *     possible object is
             *     {@link Integer }
             *     
             */
            public Integer getCommand() {
                return command;
            }

            /**
             * Sets the value of the command property.
             * 
             * @param value
             *     allowed object is
             *     {@link Integer }
             *     
             */
            public void setCommand(Integer value) {
                this.command = value;
            }

        }

    }

}
