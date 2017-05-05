package Game.Model;

/**
 *
 * @author jo-be_000
 */
public class MetaData {
    private String author = "";
    private String name = "";
    private String comment = "";

    /**
     * Gets author
     * @return author
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Sets author
     * @param author from .rle file.
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * Gets name
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name
     * @param name from .rle file.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets comment
     * @return comment
     */
    public String getComment() {
        return comment;
    }

    /**
     * Sets comment
     * @param comment from .rle file.
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

}
