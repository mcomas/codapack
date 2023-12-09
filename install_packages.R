available_packages = rownames(installed.packages())
R_repo = 'http://cran.us.r-project.org'

if (!require('rJava')) install.packages('rJava', repos = R_repo, dependencies = TRUE)
if (!require('coda.base')) install.packages('coda.base', repos = R_repo, dependencies = TRUE)
if (!require('zCompositions')) install.packages('zCompositions', repos = R_repo, dependencies = TRUE)
if (!require('fpc')) install.packages('fpc', repos = R_repo, dependencies = TRUE)
if (!require('MASS')) install.packages('MASS', repos = R_repo, dependencies = TRUE)
if (!require('knitr')) install.packages('knitr', repos = R_repo, dependencies = TRUE)