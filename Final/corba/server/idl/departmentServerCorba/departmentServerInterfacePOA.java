package server.idl.departmentServerCorba;


/**
* server/idl/departmentServerCorba/departmentServerInterfacePOA.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from server/idl/departmentServerCorba.idl
* Thursday, November 29, 2018 5:08:18 PM EST
*/

public abstract class departmentServerInterfacePOA extends org.omg.PortableServer.Servant
 implements server.idl.departmentServerCorba.departmentServerInterfaceOperations, org.omg.CORBA.portable.InvokeHandler
{

  // Constructors

  private static java.util.Hashtable _methods = new java.util.Hashtable ();
  static
  {
    _methods.put ("addCourse", new java.lang.Integer (0));
    _methods.put ("removeCourse", new java.lang.Integer (1));
    _methods.put ("listCourseAvailability", new java.lang.Integer (2));
    _methods.put ("enrolCourse", new java.lang.Integer (3));
    _methods.put ("dropCourse", new java.lang.Integer (4));
    _methods.put ("getClassSchedule", new java.lang.Integer (5));
    _methods.put ("swapCourse", new java.lang.Integer (6));
  }

  public org.omg.CORBA.portable.OutputStream _invoke (String $method,
                                org.omg.CORBA.portable.InputStream in,
                                org.omg.CORBA.portable.ResponseHandler $rh)
  {
    org.omg.CORBA.portable.OutputStream out = null;
    java.lang.Integer __method = (java.lang.Integer)_methods.get ($method);
    if (__method == null)
      throw new org.omg.CORBA.BAD_OPERATION (0, org.omg.CORBA.CompletionStatus.COMPLETED_MAYBE);

    switch (__method.intValue ())
    {
       case 0:  // server/idl/departmentServerCorba/departmentServerInterface/addCourse
       {
         String advisorID = in.read_string ();
         String courseID = in.read_string ();
         String semester = in.read_string ();
         String capacity = in.read_string ();
         String $result = null;
         $result = this.addCourse (advisorID, courseID, semester, capacity);
         out = $rh.createReply();
         out.write_string ($result);
         break;
       }

       case 1:  // server/idl/departmentServerCorba/departmentServerInterface/removeCourse
       {
         String advisorID = in.read_string ();
         String courseID = in.read_string ();
         String semester = in.read_string ();
         String $result = null;
         $result = this.removeCourse (advisorID, courseID, semester);
         out = $rh.createReply();
         out.write_string ($result);
         break;
       }

       case 2:  // server/idl/departmentServerCorba/departmentServerInterface/listCourseAvailability
       {
         String advisorID = in.read_string ();
         String semester = in.read_string ();
         String $result = null;
         $result = this.listCourseAvailability (advisorID, semester);
         out = $rh.createReply();
         out.write_string ($result);
         break;
       }

       case 3:  // server/idl/departmentServerCorba/departmentServerInterface/enrolCourse
       {
         String studentID = in.read_string ();
         String courseID = in.read_string ();
         String semester = in.read_string ();
         String $result = null;
         $result = this.enrolCourse (studentID, courseID, semester);
         out = $rh.createReply();
         out.write_string ($result);
         break;
       }

       case 4:  // server/idl/departmentServerCorba/departmentServerInterface/dropCourse
       {
         String studentID = in.read_string ();
         String courseID = in.read_string ();
         String $result = null;
         $result = this.dropCourse (studentID, courseID);
         out = $rh.createReply();
         out.write_string ($result);
         break;
       }

       case 5:  // server/idl/departmentServerCorba/departmentServerInterface/getClassSchedule
       {
         String studentID = in.read_string ();
         String $result = null;
         $result = this.getClassSchedule (studentID);
         out = $rh.createReply();
         out.write_string ($result);
         break;
       }

       case 6:  // server/idl/departmentServerCorba/departmentServerInterface/swapCourse
       {
         String studentID = in.read_string ();
         String newCourseID = in.read_string ();
         String oldCourseID = in.read_string ();
         String semester = in.read_string ();
         String $result = null;
         $result = this.swapCourse (studentID, newCourseID, oldCourseID, semester);
         out = $rh.createReply();
         out.write_string ($result);
         break;
       }

       default:
         throw new org.omg.CORBA.BAD_OPERATION (0, org.omg.CORBA.CompletionStatus.COMPLETED_MAYBE);
    }

    return out;
  } // _invoke

  // Type-specific CORBA::Object operations
  private static String[] __ids = {
    "IDL:server/idl/departmentServerCorba/departmentServerInterface:1.0"};

  public String[] _all_interfaces (org.omg.PortableServer.POA poa, byte[] objectId)
  {
    return (String[])__ids.clone ();
  }

  public departmentServerInterface _this() 
  {
    return departmentServerInterfaceHelper.narrow(
    super._this_object());
  }

  public departmentServerInterface _this(org.omg.CORBA.ORB orb) 
  {
    return departmentServerInterfaceHelper.narrow(
    super._this_object(orb));
  }


} // class departmentServerInterfacePOA
