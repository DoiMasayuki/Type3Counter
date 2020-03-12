import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

class ClonePair {
    private final Set<Fragment> fragments = new HashSet<>();
    private boolean isType3;

    ClonePair(List<String> pairStr) {
        this.isType3 = !pairStr.get(0).contains("similarity=\"100\"");
        String fragmentLStr = pairStr.get(1);
        String fragmentRStr = pairStr.get(2);
        this.fragments.add(new Fragment(fragmentLStr.split("\"")[1], Integer.valueOf(fragmentLStr.split("\"")[3]), Integer.valueOf(fragmentLStr.split("\"")[5])));
        this.fragments.add(new Fragment(fragmentRStr.split("\"")[1], Integer.valueOf(fragmentRStr.split("\"")[3]), Integer.valueOf(fragmentRStr.split("\"")[5])));
    }

    ClonePair(Fragment fragmentL, Fragment fragmentR) {
        this.fragments.add(fragmentL);
        this.fragments.add(fragmentR);
    }

    @Override
    public String toString(){
        return this.fragments.stream().map(f->f.toString()).collect(Collectors.joining());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        ClonePair clonePair = (ClonePair) o;
        return Objects.equals(this.fragments, clonePair.fragments);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.fragments);
    }

    boolean isType3() {
        return this.isType3;
    }

    /*    <clone nlines="249" similarity="100">
<source file="tomcat/java/org/apache/el/parser/ELParserTokenManager.java" startline="713" endline="1050" pcid="15855"></source>
<source file="tomcat/java/org/apache/el/parser/ELParserTokenManager.java" startline="1488" endline="1825" pcid="15869"></source>
</clone>*/
}
