package com.sample.tests;

/**
 * This class was automatically generated by the data modeler tool.
 */

public class PrimeTransaction implements java.io.Serializable
{

   static final long serialVersionUID = 1L;

   @org.kie.api.definition.type.Label(value = "Name")
   private java.lang.String name;
   @org.kie.api.definition.type.Label(value = "Date")
   private java.util.Date date;

   public PrimeTransaction()
   {
   }

   public java.lang.String getName()
   {
      return this.name;
   }

   public void setName(java.lang.String name)
   {
      this.name = name;
   }

   public java.util.Date getDate()
   {
      return this.date;
   }

   public void setDate(java.util.Date date)
   {
      this.date = date;
   }

   public PrimeTransaction(java.lang.String name, java.util.Date date)
   {
      this.name = name;
      this.date = date;
   }
   
   public String toString()
   {
	   return "<PRIMETRANSACTION: Name -> " + getName() + " Date -> " + getDate() + ">";
   }

}