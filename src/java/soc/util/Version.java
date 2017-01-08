package soc.util;

// Version.java - mchenryc@acm.org Chadwick A. McHenry
// Portions copyright (C) 2008,2010,2011,2013-2015 Jeremy D Monin <jeremy@nand.net>

import java.io.PrintStream;


/**
 * Package level version file used to keep packaging and codebase in sync. The
 * file Version.java.in is filtered to create Version.java when Callisto is
 * built using <a href="http://ant.apache.org">ant</a>.  If you are not using
 * ant to build Callisto you can do this manually by copying Version.java.in
 * to Version.java, replacing "@ VERSION @" with the "version" property value
 * in the file build.xml.
 *
 * @author <a href="mailto:mchenryc@acm.org">Chadwick A. McHenry</a>
 */
public class Version {

  /**
   * Given a version number integer such as 1109, return a human-readable string such as "1.1.09".
   * @param  versionNumber  a version number, as returned by {@link #versionNumber()},
   *           which should be 0 or an integer 1000 or higher
   * @return  version as a human-readable string such as "1.1.09"
   * @since 1.1.09
   * @see #version()
   */
  public static String version(final int versionNumber)
  {
	  return "V-JohnyAndMary";
  }

  /** Return the current version string. @see #versionNumber() */
  public static String version() {
    return "V-JohnyAndMary";
  }

  /**
   * Return the current version number.
   * @return Version integer; 1100 is version 1.1.00.
   *         If the version number cannot be read, 0 is returned.
   * @see #version()
   * @see #version(int)
   */
  public static int versionNumber() {
    return 1;
  }

  /**
   * For new game creation, return the minimum recent version number to
   * not warn during new game creation;
   * should be a version released more than a year ago.
   * If game options require a newer version, warn about that
   * in the {@code NewGameOptionsFrame} options dialog.
   *<P>
   * To view or set this version, see {@link #VERSNUM_NOWARN_MAXIMUM}.
   *
   * @return Version integer; 1108 is version 1.1.08.
   *         If the version number cannot be read, -1 is returned.
   * @see #versionNumber()
   * @since 1.1.13
   */
  public static int versionNumberMaximumNoWarn() {
    return 1;
  }

  /** Return the copyright string. */
  public static String copyright() {
    return "GPLv3.0 - Copyright Cristian Bogdan and Jeremy Monin - Custom Enrique del Pozo";
  }

  /** Return the build-number string. */
  public static String buildnum() {
    return "Codename-Yenko";
  }

  /** Return the minimum required jre. */
  public static String minJREVersion() {
    return "JVM-1.5";
  }

  /**
   * Print the JSettlers version banner and attribution text. Formerly inside {@code SOCPlayerClient}, {@code SOCServer}.
   * @param out  {@link System#out} or {@link System#err}
   * @param progname  "Java Settlers Server " or "Java Settlers Client ", including trailing space
   * @since 1.1.18
   */
  public static void printVersionText(PrintStream out, final String progname)
  {
      out.println(progname + version() +
          ", build " + buildnum() + ", (C) " + copyright());
      out.println("Network layer based on code by Cristian Bogdan; Practice Net by Jeremy Monin.");
  }

}
