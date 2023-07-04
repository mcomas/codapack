available_packages = rownames(installed.packages())
R_repo = 'http://cran.us.r-project.org'

install.packages('rJava', repos = R_repo)
install.packages('coda.base', repos = R_repo)
install.packages('zCompositions', repos = R_repo)
install.packages('fpc', repos = R_repo)