import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class BuildingPageMaker {

    private String html;
    private File imageDir;

    public BuildingPageMaker() {

        try {
            this.html = FileUtils.readFileToString(new File("src/resources/single_unit.html"), "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("");
        }

        imageDir = new File("src/resources/images");
        if (!imageDir.exists() || !imageDir.isDirectory()) {
            throw new RuntimeException("image directory not found or is not a directory!");
        }

    }

    public String createPage(final Building building) {

        System.out.println("generating page for " + building.name + "...");

        String content = html;

        content = content.replace("%%unitName%%", building.name);
        content = content.replace("%%unitAddress%%", building.address);
        content = content.replace("%%dimensions%%", building.getDimensions());
        content = content.replace("%%squareFootage%%", building.getSquareFootage());
        content = content.replace("%%numRestrooms%%", building.getRestrooms());
        content = content.replace("%%hasAC%%", building.hasAirConditioning());
        content = content.replace("%%hasThreePhase%%", building.hasThreePhase());
        content = content.replace("%%ohdDescription%%", building.overheadDoors);
        content = content.replace("%%numManDoors%%", building.getManDoors());
        content = content.replace("%%monthlyRate%%", building.getMonthlyRate());

        // Add features, if any
        if (building.features.size() > 0) {
            String features =
                    "<tr>" +
                            "   <td class=\"spec_col\">Features</td>" +
                            "   <td class=\"spec_row\">" +
                            "<ul>";

            for (String feature : building.features) {
                features += "<li>" + feature + "</li>";
            }

            features += "</ul> </td> </tr>";

            content = content.replace("%%additionalFeatures%%", features);
        } else {
            content = content.replace("%%additionalFeatures%%", "");
        }

        // Add images
        String addImages = "";
        for (String imageName : getImagesForBuildingNumber(building)) {
            addImages += "<img src=\"http://www.bunnsvillage.com/images/" + imageName + "\" class=\"picture\">";
        }

        content = content.replace("%%images%%", addImages);

        return content;

    }

    private List<String> getImagesForBuildingNumber(final Building building) {

        // TODO: return exterior picks of sub-units
        if (building.isSubunit) {
            return Arrays.stream(Objects.requireNonNull(imageDir.listFiles()))
                    .filter(file -> file.getName().startsWith(building.name) || file.getName().startsWith(building.getMasterBuildingName()))
                    .map(File::getName)
                    .collect(Collectors.toList());
        } else {
            return Arrays.stream(Objects.requireNonNull(imageDir.listFiles()))
                    .filter(file -> file.getName().startsWith(building.name))
                    .map(File::getName)
                    .collect(Collectors.toList());
        }

    }

}
