package se.phlogiston.jopt;

import java.lang.reflect.Field;
import java.util.*;

/**
 * Classname: CliParse
 * Author(s): Erik Svensson
 * Date: Sep 23, 2009
 * Version: $Id$
 * Copyright Notice: 2009 Erik Svensson
 * Description:
 *
 *  CliParse parse command line options. It tries to emulate POSIX getopt to some extent
 *  cliParse can handle: short options, long options and option groups (for short options only
 * option groups for long options are nonsensical). An option may have an argument or an argument list.
 * An argument list is a sequence of arguments separated by ','. There may NOT be spaces between the '','.
 *
 */
public class CliParse<E extends Enum<E>> {

    private LinkedList<String> argList;

    private ArrayList<OptionsRecord<E>> orList = new ArrayList<OptionsRecord<E>>();

    private HashMap<String, OptionsRecord<E>> longMap = new HashMap<String, OptionsRecord<E>>();

    private HashMap<String, OptionsRecord<E>> baseMap = new HashMap<String, OptionsRecord<E>>();

    private HashMap<String, OptionsRecord<E>> conflictMap = new HashMap<String, OptionsRecord<E>>();

    private LinkedList<String> operands = new LinkedList<String>();

    public CliParse(Class<E> pClass, String[] pArgs ) {
        Class<E> enumClass = pClass;

        argList = new LinkedList<String>(Arrays.asList(pArgs));

        E[] enums = pClass.getEnumConstants();

        Field f = null;
        try {
            for(E e : enums) {
                f = pClass.getDeclaredField(e.name());
                CLIOPTIONS anno = f.getAnnotation(CLIOPTIONS.class);
                OptionsRecord<E> or = new OptionsRecord<E>(e);
                if (anno.shortOpt().length() > 1 ) {
                    throw new IllegalArgumentException("Error in option declaration. Declared short option cannot be more than one character: "+anno.shortOpt());
                }
                or.setShortName(anno.shortOpt());
                or.setLongName(anno.longOpt());
                or.setOptArg(anno.argType());
                or.setHelp(anno.help());

                if (anno.notWith().length() > 1) {
                    or.setNotWith(anno.notWith().split(","));
                }

                if (anno.shortOpt() != null) {
                    baseMap.put(anno.shortOpt(), or);
                }

                if (!anno.longOpt().equals("")) {
                    longMap.put(anno.longOpt(),or);
                }
            }

        } catch(NoSuchFieldException nsfe) {
            System.out.println("exception ");
        }

    }

    public List<OptionsRecord<E>> parse() throws MissingArgumentException {

        OptionsRecord<E> currentOpt = null;

        while (!argList.isEmpty()) {

            String current = argList.poll();
            System.out.println("Current opt is "+ current);
            if (isFlag(current)) {
                // check if the flag needs expansion
                if (needsExpansion(current)){
                    expand(current);
                    continue;
                }

                currentOpt = getFlag(current);
                System.out.println("Identified as "+current);
                if (currentOpt == null) {
                    throw new MissingArgumentException("Unknown option : "+current);
                }
            } else {
                // we are at the end here. No more options and no more arguments.
                // There are however operands else we would have exited already.
                // we stuff the operands into a new list that can be accessed through
                // the getRemainingArguments call

                operands.add(current);
                continue;

                //return orList;
            }
            // here
            if (currentOpt.getArgumentOption() != OptArgs.NONE ) {
                String argument = argList.getFirst();
                // check if the argument really is an argument and not a flag
                if (!isFlag(argument)) {
                    // set the options argument
                    currentOpt.setArg(argument);
                    // and remove it from the list
                    argList.removeFirst();
                } else {
                    // throw an exception if the arg is mandatory
                    if (currentOpt.getArgumentOption() == OptArgs.MANDATORY) {
                        System.out.println("Mandatory argument required but not found");
                        throw new MissingArgumentException("Mandatory argument required but not found for option ");
                    }
                }
            }
            orList.add(currentOpt);
        }

        return orList;
    }

    public List<String> getArguments() {
        return argList;
    }

    protected OptionsRecord<E> getFlag(String pOption) {
        OptionsRecord<E> or = null;

        if (pOption.startsWith("--")) {
            String tmp = pOption.substring(2);
            or = longMap.get(tmp);
        } else if (pOption.startsWith("-")) {
            String tmp = pOption.substring(1);
            or = baseMap.get(tmp);

        }
        
        return or;
    }

    public List<String> getRemainingArguments() {
        return operands;
    }

    private boolean isFlag(String pInQuestion) {

        return (pInQuestion.startsWith("-") || pInQuestion.startsWith("--"));
    }


    private boolean needsExpansion(String pArg) {
        return (pArg.startsWith("-") && ! pArg.startsWith("--") && pArg.substring(1).length() > 1); 
    }

    private void expand(String pArg) {
        // the args without the '-' indicator
        // and create a char arrray of it
        char[]  chars = pArg.substring(1).toCharArray();

        // iterate over the char array and add the chars to the list in front
        // and remember to add a '-' to each arg. The args needs to be added in the same
        // order as they appeared on the cl as the last arg might have an argument
        for (int i = chars.length -1 ; i >= 0 ; i--) {
            argList.addFirst("-"+chars[i]);
        }
    }
}
