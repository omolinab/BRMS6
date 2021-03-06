package com.sample.tests;

/**
 * This class was automatically generated by the data modeler tool.
 */

public class Reconcilation implements java.io.Serializable
{

   static final long serialVersionUID = 1L;

   @org.kie.api.definition.type.Label(value = "Name")
   private java.lang.String name;
   @org.kie.api.definition.type.Label(value = "Prime Transaction Object")
   private PrimeTransaction pTransactionObj;
   @org.kie.api.definition.type.Label(value = "Date")
   private java.util.Date date;

   public Reconcilation()
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

   public PrimeTransaction getpTransactionObj()
   {
      return this.pTransactionObj;
   }

   public void setpTransactionObj(PrimeTransaction pTransactionObj)
   {
      this.pTransactionObj = pTransactionObj;
   }

   public java.util.Date getDate()
   {
      return this.date;
   }

   public void setDate(java.util.Date date)
   {
      this.date = date;
   }

   public Reconcilation(java.lang.String name, PrimeTransaction pTransactionObj, java.util.Date date)
   {
      this.name = name;
      this.pTransactionObj = pTransactionObj;
      this.date = date;
   }

   public String toString()
   {
	   return "[RECONCILATION: Name -> " + getName() + " pTransactionObj -> " + getpTransactionObj() + " Date -> " + getDate() + "]";
   }
   
}