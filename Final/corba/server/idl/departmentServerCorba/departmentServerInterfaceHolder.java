package server.idl.departmentServerCorba;

/**
* server/idl/departmentServerCorba/departmentServerInterfaceHolder.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from server/idl/departmentServerCorba.idl
* Thursday, November 29, 2018 5:08:18 PM EST
*/

public final class departmentServerInterfaceHolder implements org.omg.CORBA.portable.Streamable
{
  public server.idl.departmentServerCorba.departmentServerInterface value = null;

  public departmentServerInterfaceHolder ()
  {
  }

  public departmentServerInterfaceHolder (server.idl.departmentServerCorba.departmentServerInterface initialValue)
  {
    value = initialValue;
  }

  public void _read (org.omg.CORBA.portable.InputStream i)
  {
    value = server.idl.departmentServerCorba.departmentServerInterfaceHelper.read (i);
  }

  public void _write (org.omg.CORBA.portable.OutputStream o)
  {
    server.idl.departmentServerCorba.departmentServerInterfaceHelper.write (o, value);
  }

  public org.omg.CORBA.TypeCode _type ()
  {
    return server.idl.departmentServerCorba.departmentServerInterfaceHelper.type ();
  }

}