package Internal.Managing.Commands;

import Internal.Managing.Core.CommandManager;

import java.util.Formatter;

/**
 * A Category for the {@link Command} object. This contains the name of the category, and whether or not it is hidden.
 * <h1>Usage:</h1>
 * You init it like any other command setting:
 * <blockquote><code><pre>
 * super.category = new Category("MyCategory" # This is the name of the category); # This is the usual constructor for non-hidden commands. (Hidden is false by default)
 * </pre></code></blockquote>
 * Or, if you want to make it hidden, init it like this: (Hidden is set to false by default)
 * <blockquote><code><pre>
 * super.category = new Category("MyHiddenCategory", true); # The category is now hidden in the default help command.
 * </pre></code></blockquote>
 * If you use the second constructor, it'll look like this:
 * <blockquote><code><pre>
 * -------------------------------------------------
 * Commands:
 *    MyOtherCommand:
 *      ...
 *      Category: MyCategory
 *   # Notice that the hidden category is not shown.
 * -------------------------------------------------
 * </pre></code></blockquote>
 * However, if you set that category to not be hidden (ie. not setting it at all), it will show:
 * <blockquote><code><pre>
 * super.category = new Category("MyHiddenCategory", false); # You can set it to false as well
 * -------------------------------------------------
 * Commands:
 *    MyOtherCommand:
 *       ...
 *       Category: MyCategory
 *    MyNonHiddenCategory:
 *       ...
 *       Category: MyHiddenCategory
 *       ...
 * -------------------------------------------------
 * </pre></code></blockquote>
 */
public class Category {
    private String name;
    private boolean hidden;

    /** Default constructor for this object. */
    public Category() {
        this.hidden = false;
    }

    /**
     * Constructor that only sets the name. Hidden boolean is set to false as default.
     * @param name The name of the Category.
     */
    public Category(String name) {
        this.name = name;
        this.hidden = false;
    }

    /**
     * Full Constructor. Use this if you want to make this category hidden, otherwise use .
     * @param name The name of the category
     * @param hidden Whether or not the category is hidden
     */
    public Category(String name, boolean hidden) {
        this.name = name;
        this.hidden = hidden;
    }

    /**
     * Gets the name of this category.
     * @return The name of this category.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Sets the name of this category.
     * @param name The new name of this category
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Checks whether or not this category is hidden.
     * @return Whether or not this category is hidden
     */
    public boolean isHidden() {
        return this.hidden;
    }

    /**
     * Sets whether or not this category is hidden.
     * @param hidden Whether or not this category is hidden
     */
    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    /**
     * Converts this object to a string, using this library's standard object format: { var1=value1, ... }
     * @return A String representing this teacher.
     */
    public String toString() {
        return new Formatter().format("{ name=%s, hidden=%s }", name, hidden).toString();
    }

    public String JSON() {
        return new Formatter().format("{ 'name': '%s', 'hidden': '%s' }", name, hidden).toString();
    }
}
