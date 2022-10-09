package org.example;

import org.alicebot.ab.*;
import org.alicebot.ab.utils.IOUtils;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * Hello world!
 *
 */
public class ChatBot
{
    private static final List<String> QUIT = List.of( "quit", "end", "bye", "goodbye" );
    private static final boolean TRACE_MODE = false;

    private Bot bot;
    private Chat chatSession;

    private boolean running = true;

    public ChatBot() {
        MagicBooleans.trace_mode = TRACE_MODE;
        String resourcesPath = getResourcesPath();
        bot = new Bot("super", resourcesPath);
        writeAiml( resourcesPath );
        chatSession = new Chat(bot);
        bot.brain.nodeStats();
    }

    public void run() {
        try {
            intro();
            while ( running ) {
                System.out.print("You : ");
                String textLine = IOUtils.readInputTextLine();
                if( hasQuit(textLine) ) {
                    running = false;
                }
                else if (noInput(textLine)) {
                    textLine = MagicStrings.null_input;
                }
                else {
                    String response = tidyResponse( chatSession.multisentenceRespond(textLine) );
                    System.out.println("Robot : " + response);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void intro() {
        System.out.println( "Hi, lets have a chat, type one of the following to quit " + QUIT );
    }

    private String tidyResponse(String textLine) {
        return textLine.replaceAll("&lt;", "<").replaceAll( "&gt;", ">" );
    }

    private boolean noInput(String textLine) {
        return (textLine == null) || (textLine.length() < 1);
    }

    private static boolean hasQuit( String input ) {
        return QUIT.contains(input.toLowerCase());
    }

    private void writeAiml( String resourcesPath ) {
        bot.writeAIMLFiles();
    }

    private String getResourcesPath() {
        File currDir = new File(".");
        Path pt = Paths.get("src", "main", "resources");
        return new File( currDir, pt.toString() ).getAbsolutePath();
    }
}
