import java.io.*;
import groovy.io.*;
import java.util.Calendar.*;
import java.text.SimpleDateFormat;
import hudson.model.*;

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


        def date = new Date();
        def sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        writer.writeLine("Data and Time is: " + sdf.format(date));

        // interpolate the build from env var
        writer.writeLine("Build Number is ${BUILD_NUMBER}");

        // changesets
        def changeLogSets = currentBuild.changeSets;
        for (change in changeLogSets) {
            def entries = change.items;
            for (entry in entries) {
                echo "${entry.commitId} by ${entry.author} on ${new Date(entry.timestamp)}: ${entry.msg}";
                for (file in entry.affectedFiles) {
                    echo "  ${file.editType.name} ${file.path}";
                }
            }
        }
    }
    if (config.changes != "false") {
        echo "changes";
    }
}
