package org.omg.DynamicAny;


/**
* org/omg/DynamicAny/DynValueCommonOperations.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from /Users/java_re/workspace/8-2-build-macosx-x86_64/jdk8u111/7883/corba/src/share/classes/org/omg/DynamicAny/DynamicAny.idl
* Thursday, September 22, 2016 3:33:15 PM PDT
*/


/**
    * DynValueCommon provides operations supported by both the DynValue and DynValueBox interfaces.
    */
public interface DynValueCommonOperations  extends org.omg.DynamicAny.DynAnyOperations
{

  /**
          * Returns true if the DynValueCommon represents a null value type.
          */
  boolean is_null ();

  /**
          * Changes the representation of a DynValueCommon to a null value type.
          */
  void set_to_null ();

  /**
          * Replaces a null value type with a newly constructed value. Its components are initialized
          * to default values as in DynAnyFactory.create_dyn_any_from_type_code.
          * If the DynValueCommon represents a non-null value type, then this operation has no effect. 
          */
  void set_to_value ();
} // interface DynValueCommonOperations
