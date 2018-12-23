import java.io.File;
import java.util.Objects;

/**
 * Created by Matin Afkhami on 5/20/2017.
 * Java 10 Recommended.
 */
class NameFindAndReplace {
    private int totalFiles=0, filesToRename=0, fileIndex=-1;
    private String[][] log;
    private String targetKeyword, replacementKeyword;
    private final File file;
    private File dir;
    private String newDir;
    private boolean isRegex;

    NameFindAndReplace(File file, String targetKeyword, String replacementKeyword, boolean isRegex) throws Exception {
        if (file==null || targetKeyword==null)
            throw new Exception("Inputs must not be null.");
        if (!file.exists())
            throw new Exception("File or directory not exists.");
        if (replacementKeyword == null)
            replacementKeyword = "";
        this.file = file;
        this.targetKeyword = targetKeyword;
        this.replacementKeyword = replacementKeyword;
        this.isRegex = isRegex;

        filesCounter(file);
    }

    int getFilesToRename(){
        return this.filesToRename;
    }

    int getTotalFiles(){
        return this.totalFiles;
    }

    String getCurrentPath(){
        if (log==null || this.log.length==0){
            return this.file.getPath();
        }else if (new File(log[0][0]).getPath().compareTo(this.file.getPath())==0){
            return log[0][1];
        }
        return this.file.getPath();
    }


///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    void replaceKeyword() {
        log = new String[filesToRename][2];
        fileIndex = -1;
        replaceKeyword(this.file);
    }

    private void replaceKeyword(File file) {
        this.dir = file;
        conditionalRename();
        this.dir = new File(newDir);
        if(this.dir.isDirectory()){
            File[] dirFiles = this.dir.listFiles();
            if (dirFiles != null) {
                for (File eachFile : dirFiles) {
                    this.dir = eachFile;
                    if (eachFile.isDirectory()){
                        replaceKeyword(this.dir);
                    } else {
                        conditionalRename();
                    }
                }
            }
        }
    }

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void conditionalRename(){
        if (isRegex) {
            conditionalRenameRegex();
        }else {
            conditionalRenameKeyword();
        }
    }

    private void conditionalRenameKeyword() {
        if (dir.getName().contains(targetKeyword)){
            this.newDir = dir.getParent() + "/" + dir.getName().replace(targetKeyword,replacementKeyword);
            int suffix = getSuffix(this.dir,this.newDir);
            conditionalRenameBoth(suffix);
            if (dir.renameTo(new File(this.newDir))) {
                fileIndex++;
                log[fileIndex][0] = this.dir.toString();
                log[fileIndex][1] = this.newDir;
            }
        }else{
            this.newDir = this.dir.toString();
        }
    }

    private void conditionalRenameRegex() {
        if (stringContainsRegex(dir.getName(),targetKeyword)){
            this.newDir = dir.getParent() + "/" + dir.getName().replaceAll(targetKeyword, replacementKeyword);
            int suffix = getSuffix(this.dir,this.newDir);
            conditionalRenameBoth(suffix);
            if (dir.renameTo(new File(this.newDir))) {
                fileIndex++;
                log[fileIndex][0] = this.dir.toString();
                log[fileIndex][1] = this.newDir;
            }
        }else{
            this.newDir = this.dir.toString();
        }
    }

    private void conditionalRenameBoth(int suffix) {
        if (suffix!=0) {
            if (this.dir.isDirectory())
                this.newDir = this.newDir + "(" + suffix + ")";
            else{
                String newDirName = new File(this.newDir).getName();
                if (newDirName.contains(".")){
                    String newDirParent = new File(this.newDir).getParent();
                    this.newDir = newDirParent + "/" + newDirName.substring(0,newDirName.lastIndexOf('.')) +
                            "(" + suffix + ")" + newDirName.substring(newDirName.lastIndexOf('.'));
                }else{
                    this.newDir = this.newDir + "(" + suffix + ")";
                }
            }
        }
    }

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    void rollbackKeyword() {
        if (log==null || this.log.length==0){
            return;
        }
        for (int i=log.length-1; i>=0; i--){
            if (new File(log[i][1]).renameTo(new File(log[i][0]))) {
                fileIndex--;
            }
        }
        log = null;
    }

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void filesCounter(File dir) {
        if (isRegex){
            filesCounterRegex(dir);
        }else{
            filesCounterKeyword(dir);
        }
    }

    private void filesCounterKeyword(File dir) {
        totalFiles++;
        if (dir.getName().contains(targetKeyword)){
            filesToRename++;
        }
        if (dir.isDirectory()){
            File[] files = dir.listFiles();
            if (files != null) {
                if(files.length!=0) {
                    for (File eachFile : files) {
                        filesCounterKeyword(eachFile);
                    }
                }
            }
        }
    }

    private void filesCounterRegex(File dir) {
        totalFiles++;
        if (stringContainsRegex(dir.getName(), targetKeyword)){
            filesToRename++;
        }
        if (dir.isDirectory()){
            File[] files = dir.listFiles();
            if (files != null) {
                if(files.length!=0) {
                    for (File eachFile : files) {
                        filesCounterRegex(eachFile);
                    }
                }
            }
        }
    }

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private static boolean fileArrayContains(File[] src, File dst){
        for (File aSrc : src) {
            if (dst.getName().equals(aSrc.getName()))
                return true;
        }
        return false;
    }

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    static private int getSuffix(File dir, String newDir) {
        int suffix = 0;
        StringBuilder newDirTemp = new StringBuilder(newDir);
        while ( fileArrayContains(Objects.requireNonNull(dir.getParentFile().listFiles()),new File(newDirTemp.toString())) ) {
            suffix++;
            newDirTemp = new StringBuilder(newDir);
            if (dir.isDirectory())
                newDirTemp.append("(").append(suffix).append(")");
            else{
                String newDirName = new File(newDir).getName();
                if (newDirName.contains(".")){
                    String newDirParent = new File(newDir).getParent();
                    newDirTemp.append(newDirParent).append("/")
                            .append(newDirName, 0, newDirName.lastIndexOf('.'))
                            .append("(").append(suffix).append(")")
                            .append(newDirName.substring(newDirName.lastIndexOf('.')));
                }else{
                    newDirTemp.append(newDirTemp).append("(").append(suffix).append(")");
                }
            }
        }
        return suffix;
    }

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private static boolean stringContainsRegex(String str, String regex) {
        for (int i = 0; i < str.length(); i++) {
            for (int j = str.length()-1; j > i; j--) {
                if (str.substring(i, j).matches(regex)) return true;
            }
        }
        return false;
    }

}