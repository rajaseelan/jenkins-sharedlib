import java.io.*;
import groovy.io.*;

// cannot survive jenkins restart
@NonCPS
// format to make it a global function
def call (Map config=[:]) {
    def String dir_path = "/home/raja/code/jenkins-go";

    def dir = new File(pwd());

    new File(dir.path + "/releasenotes.txt").withWriter('utf-8')
    {
        writer ->
            dir.eachFileRecurse(FileType.ANY) { file ->
            if (file.isDirectory()) {
                writer.writeLine(file.name);
            }
            else
            {
                writer.writeLine(file.name + '\t' + file.length());
            }
        }
    }

    if (config.changes != "false") {
        echo "changes";
    }

    def date = new Date();
    def sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
}
