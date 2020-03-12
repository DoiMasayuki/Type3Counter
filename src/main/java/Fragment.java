import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

class Fragment {
    private final Path path;
    private final int startline;
    private final int endline;

    Fragment(String path, int startline, int endline) {
        this.path = Paths.get(path);
        this.startline = startline;
        this.endline = endline;
    }

    @Override
    public String toString() {
        return "Fragment{" +
                "path=" + this.path +
                ", startline=" + this.startline +
                ", endline=" + this.endline +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        Fragment fragment = (Fragment) o;
        return this.startline == fragment.startline &&
                this.endline == fragment.endline &&
                Objects.equals(this.path, fragment.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.path, this.startline, this.endline);
    }

}
