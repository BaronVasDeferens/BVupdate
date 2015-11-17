/*
find ./ -type d | while read line ; do echo $line ; pushd "$line"; cat /dev/null > md5sums; find ./ -type f -maxdepth 1 -exec md5sum {} \; | grep -vE '^[a-z0-9]{32}  \./md5sums$' >> ~/Desktop/md5sums.txt; if [[ $(file md5sums) == "md5sums: empty" ]] ; then rm -f md5sums; fi; popd ; done

 */
package dupefinder;

/**
 *
 * @author skot
 */
public class DupeFinder {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
    }
    
}
