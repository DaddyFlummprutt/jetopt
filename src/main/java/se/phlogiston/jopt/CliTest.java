package se.phlogiston.jopt;

import java.util.List;

/**
 * Classname: CliTest
 * Author(s): Erik Svensson
 * Date: Sep 23, 2009
 * Version: $Id$
 * Copyright Notice: 2010 Erik Svensson
 * Description:
 *
 * Small test class that uses CliParse. It implements 6 cli options.
 */
public class CliTest {

    // the class representing the command line options. It can have any name
    public enum CliBase {
        @CLIOPTIONS(shortOpt = "f",argType = OptArgs.MANDATORY)
        FILE,
        @CLIOPTIONS(shortOpt = "h",longOpt = "host",argType = OptArgs.MANDATORY)
        HOST,
        @CLIOPTIONS(shortOpt = "p",longOpt = "port", argType = OptArgs.MANDATORY,help = "Port nr")
        PORT,
        @CLIOPTIONS(shortOpt = "a", notWith="h,p")
        A_OPT,
        @CLIOPTIONS(shortOpt="b")
        B_OPT,
        @CLIOPTIONS(shortOpt = "c")
        C_OPT,
        ;
    }

    public static void main(String[] args) {

        CliParse<CliBase> cp = new CliParse<CliBase>(CliBase.class,args);
        try {

            List<OptionsRecord<CliBase>> l = cp.parse();


            for(OptionsRecord o : l){
                System.out.println("cl argument is "+o);
            }

            List<String> argList = cp.getRemainingArguments();

            for (String s : argList) {
                System.out.println(" argument is : "+s);
            }
        } catch(Exception mae) {
            System.out.println("exception caught"+mae);
        }

    }


}

/*


  At work I wrote a new server that had to take a whole slew of command line arguments. Not wanting to write a parser for that myself i googled on it and found a couple of packages that could do that. However, they all lacked a certain sense of elegance and java-ness. Plus some of them were not really type-safe and most of them required the user to use string comparison.
So, when thinking about this I came upon the notion of regarding the comand line arguments as enums.
Then the programmer would declare an enum class with all the info needed and could iterate over a enum set to get the cli options that was submitted to the app. There were some problems with this but could be fixed by annotating the enums. This didn't seem to all that common (once again google came to the rescue) and I got curious if it could be done. To make it short, it could indeed be done and is quite elegant too. So I wrote the cli parser using annotated enumerations and I thought maybe other people would be interested in this. I've found quite a few uses for the technique, the latest amusingly enough in a developerworks piece about java.util.Scanner where the cli package idea would be great!

This is meant for developers and tech level would be intermediate I would guess.

*/