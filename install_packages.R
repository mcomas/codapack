available_packages = rownames(installed.packages())
R_repo = 'http://cran.us.r-project.org'

install.packages('rJava', repos = R_repo, dependencies = TRUE)
install.packages('coda.base', repos = R_repo, dependencies = TRUE)
install.packages('zCompositions', repos = R_repo, dependencies = TRUE)
install.packages('fpc', repos = R_repo, dependencies = TRUE)
install.packages('MASS', repos = R_repo, dependencies = TRUE)
install.packages('knitr', repos = R_repo, dependencies = TRUE)