package se.phlogiston.jopt;

import java.util.Arrays;

/**
 * Classname: OptionsRecord
 * Author(s): Erik Svensson
 * Date: Sep 29, 2010
 * Version: $Id$
 * Copyright Notice: 2010 Erik Svensson
 * Description:
 */
public class OptionsRecord<E extends Enum<E>> {

    public enum ArgumentType {
        OPTION, ARGUMENT,;
    }

    private E myEnum;

    private ArgumentType argumentType; 

    private String shortName;
    private String longName;
    private OptArgs argumentOption = OptArgs.NONE;
    private String arg;
    private String help;
    private String[] exclusion;

    public void setArgumentType(ArgumentType pType) {
        argumentType = pType;
    }

    protected OptionsRecord(E pEnum) {
        myEnum = pEnum;
    }

    protected void setShortName(String pShort)  {
        shortName = pShort;
    }

    protected void setLongName(String pLongName) {
        longName = pLongName;
    }

    protected void setArg(String pArg) {
        arg = pArg;
    }

    protected void setHelp(String pHelp) {
        help = pHelp;
    }

    protected void setOptArg(OptArgs pOption) {
        argumentOption = pOption;
    }

    public E getEnum() {
        return myEnum;
    }

    public String getShortName() {
        return shortName;
    }

    public String getLongName() {
        return longName;
    }

    public OptArgs getArgumentOption() {
        return argumentOption;
    }

    public String getArg() {
        return arg;
    }

    public String getHelp() {
        return help;
    }

    public void setNotWith(String[] pExclusion){
        exclusion = pExclusion;
    }

    public String[] getNotWith() {
        return exclusion;
    }

    @Override
    public String toString() {
        return "OptionsRecord{" +
                "myEnum=" + myEnum +
                ", argumentType=" + argumentType +
                ", shortName='" + shortName + '\'' +
                ", longName='" + longName + '\'' +
                ", argumentOption=" + argumentOption +
                ", arg='" + arg + '\'' +
                ", help='" + help + '\'' +
                ", exclusion=" + (exclusion == null ? null : Arrays.asList(exclusion)) +
                '}';
    }
}
