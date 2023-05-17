library(stringr)
menus = yaml::read_yaml('codapack_structure.yaml')
cat(rjson::toJSON(menus, indent = 1), file='codapack_structure_human_readable.json')
cat(rjson::toJSON(menus, indent = 1))
cat(rjson::toJSON(menus, indent = 0), file='codapack_structure.json')
#k = unname(unlist(menus))
#kl = paste0(str_sub(tolower(k), 1, 1), str_sub(k, 2, -1))
#cat(sprintf("case \"%s\":\n\tif(%sMenu == null) %sMenu = new %sMenu(this);\n\t%sMenu.updateMenuDialog();\n\t%sMenu.setVisible(true);\n", k, kl, kl, k,kl,kl))

    