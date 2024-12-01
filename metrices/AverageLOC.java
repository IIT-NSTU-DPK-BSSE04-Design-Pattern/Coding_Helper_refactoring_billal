public class AverageLOC {
    private ProjectReader projectReader = new ProjectReader();
    
    public void totalClass(String path) {
        try {
            projectReader.fileRead(path, ProjectType.PROJECT_ONE);
            int totalClass = projectReader.classCount;
            
            for (String filePath : projectReader.filename) {
                new LineOfCode().countLines(filePath);
            }
            
            double average = (double) LineOfCode.totalLineOfProject / projectReader.filename.size();
            System.out.println("\tAverage LOC in a class: " + BigDecimal.valueOf(average).setScale(2, RoundingMode.HALF_UP));
            
            resetProjectReader();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void resetProjectReader() {
        projectReader.classCount = 0;
        projectReader.filename.clear();
        LineOfCode.totalLineOfProject = 0;
    }
}
