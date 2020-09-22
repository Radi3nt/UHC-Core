package fr.radi3nt.loupgarouuhc.classes.roles;

import fr.radi3nt.loupgarouuhc.classes.lang.translations.Lang;
import fr.radi3nt.loupgarouuhc.classes.roles.roles.LoupGarou.LGBlanc;
import fr.radi3nt.loupgarouuhc.classes.roles.roles.LoupGarou.LGFeutre;
import fr.radi3nt.loupgarouuhc.classes.roles.roles.LoupGarou.LGInfect;

import static fr.radi3nt.loupgarouuhc.LoupGarouUHC.langWarperInstance;

public enum RoleSort {


	VILLAGER(WinType.VILLAGE,RoleType.VILLAGER, Lang.VILLAGER.getName(), Lang.VILLAGER.getShortDesc(), Lang.VILLAGER.getDesc(), Lang.VILLAGER.getId()),
	LOUP_GAROU(WinType.LOUP_GAROU,RoleType.LOUP_GAROU, Lang.LOUP_GAROU.getName(), Lang.LOUP_GAROU.getShortDesc(), Lang.LOUP_GAROU.getDesc(), Lang.LOUP_GAROU.getId()),
	PETITE_FILLE(WinType.VILLAGE,RoleType.VILLAGER, Lang.PETITE_FILLE.getName(), Lang.PETITE_FILLE.getShortDesc(), Lang.PETITE_FILLE.getDesc(), Lang.PETITE_FILLE.getId()),
	VP_LOUP(WinType.LOUP_GAROU,RoleType.LOUP_GAROU, Lang.VP_LOUP.getName(), Lang.VP_LOUP.getShortDesc(), Lang.VP_LOUP.getDesc(), Lang.VP_LOUP.getId()),
	VOYANTE(WinType.VILLAGE,RoleType.VILLAGER, Lang.VOYANTE.getName(), Lang.VOYANTE.getShortDesc(), Lang.VOYANTE.getDesc(), Lang.VOYANTE.getId()),
	LGBLANC(WinType.SOLO, RoleType.LOUP_GAROU, Lang.LGBLANC.getName(), Lang.LGBLANC.getShortDesc(), Lang.LGBLANC.getDesc(), Lang.LGBLANC.getId()),
	MONTREUR_OURS(WinType.VILLAGE,RoleType.VILLAGER, Lang.MONTREUR_OURS.getName(), Lang.MONTREUR_OURS.getShortDesc(), Lang.MONTREUR_OURS.getDesc(), Lang.MONTREUR_OURS.getId()),
	ASSASSIN(WinType.SOLO,RoleType.NEUTRAL, Lang.ASSASSIN.getName(), Lang.ASSASSIN.getShortDesc(), Lang.ASSASSIN.getDesc(), Lang.ASSASSIN.getId()),
	LOUP_PERFIDE(WinType.LOUP_GAROU,RoleType.LOUP_GAROU, Lang.LOUP_PERFIDE.getName(), Lang.LOUP_PERFIDE.getShortDesc(), Lang.LOUP_PERFIDE.getDesc(), Lang.LOUP_PERFIDE.getId()),
	SORCIERE(WinType.VILLAGE,RoleType.VILLAGER, Lang.SORCIERE.getName(), Lang.SORCIERE.getShortDesc(), Lang.SORCIERE.getDesc(), Lang.SORCIERE.getId()),
	GUARD(WinType.VILLAGE,RoleType.VILLAGER, "Guard", "", "", ""),
	LG_INFECTE(WinType.VILLAGE,RoleType.LOUP_GAROU, "Loup infecte", "", "", ""),
	CUPIDON(WinType.VILLAGE, RoleType.NEUTRAL, "Cupidon", "", "", ""),
	LG_FEUTRE(WinType.LOUP_GAROU, RoleType.LOUP_GAROU, Lang.LOUP_FEUTRE.getName(), Lang.LOUP_FEUTRE.getShortDesc(), Lang.LOUP_FEUTRE.getDesc(), Lang.LOUP_FEUTRE.getId()),
	MINEUR(WinType.VILLAGE, RoleType.VILLAGER, Lang.MINEUR.getName(), Lang.MINEUR.getShortDesc(), Lang.MINEUR.getDesc(), Lang.MINEUR.getId());





	public final WinType winType;
	public final RoleType roleType;
	public final String name;
	public final String shortDescription;
	public final String roleDescription;
	public final String id;

	RoleSort(WinType win, RoleType roleType, String name, String shortDesc, String description, String id) {
		this.winType = win;
		this.roleType = roleType;
		this.name = name;
		this.shortDescription = shortDesc;
		this.roleDescription = description;
		this.id=id;
	}

}
